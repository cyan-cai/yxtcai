package com.java.yxt.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.dao.*;
import com.java.yxt.dto.CustomerDto;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.feign.CustomerFeignService;
import com.java.yxt.feign.ResultObject;
import com.java.yxt.feign.vo.AddOrgVo;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.po.UpLoadErrorPo;
import com.java.yxt.service.CustomerService;
import com.java.yxt.service.DictService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.SequenceWorker;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * CustomerServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description 客户管理
 * @Package com.java.yxt.service.impl
 * @date 2020/9/15
 */
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    TerminalCallbackMapper terminalCallbackMapper;

    @Autowired
    CustomerFeignService customerFeignService;

    @Autowired
    CustomerTenantMapper customerTenantMapper;

    @Autowired
    UploadErrorMapper uploadErrorMapper;

    @Autowired
    ServiceMapper serviceMapper;

    @Autowired
    ApiMapper apiMapper;
    @Autowired
    RedissonClient redissonUtils;

    @Autowired
    DictService dictService;
    //校验IP地址正则
    public static final String IPREGEX="^(((((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/(\\d|[1-2]\\d|3[0-2]))?)|(([\\da-fA-F]{1,4}:){6}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(::([\\da-fA-F]{1,4}:){0,4}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:):([\\da-fA-F]{1,4}:){0,3}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){2}:([\\da-fA-F]{1,4}:){0,2}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){3}:([\\da-fA-F]{1,4}:){0,1}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){4}:((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(:((:[\\da-fA-F]{1,4}){1,6}|:)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|([\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,5}|:)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){2}((:[\\da-fA-F]{1,4}){1,4}|:)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,3}|:)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){4}((:[\\da-fA-F]{1,4}){1,2}|:)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){5}:([\\da-fA-F]{1,4})?(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){6}:(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?))[\\n])*((((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/(\\d|[1-2]\\d|3[0-2]))?)|(([\\da-fA-F]{1,4}:){6}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(::([\\da-fA-F]{1,4}:){0,4}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:):([\\da-fA-F]{1,4}:){0,3}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){2}:([\\da-fA-F]{1,4}:){0,2}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){3}:([\\da-fA-F]{1,4}:){0,1}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){4}:((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(:((:[\\da-fA-F]{1,4}){1,6}|:)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|([\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,5}|:)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){2}((:[\\da-fA-F]{1,4}){1,4}|:)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,3}|:)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){4}((:[\\da-fA-F]{1,4}){1,2}|:)(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){5}:([\\da-fA-F]{1,4})?(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?)|(([\\da-fA-F]{1,4}:){6}:(\\/([1-9]?\\d|(1([0-1]\\d|2[0-8]))))?))$";
    //校验客户号码正则
    public static final String PHONENUMREGEX="^\\+{0,1}[0-9]+\\-{0,1}[0-9]+$";
    //校验导入文件名正则
    public static final String FILENAMEREGEX="^customer{1}.*\\.xls{1}$";
    /**
     * 新增插入
     * @param customerVo
     * @return
     */
    @SetCreaterName
    @Transactional(rollbackFor = Exception.class)
    public int insertCustomer(CustomerVo customerVo) {
        customerVo.setStatus(1);
        //同步客户与租户关系表
        CustomerTenantVo customerTenantVo=new CustomerTenantVo();
        customerTenantVo.setTenantId(customerVo.getTenantId());
        customerTenantVo.setCustomerCode(customerVo.getCustomerCode());
        customerTenantMapper.insert(customerTenantVo);

        int count=0;
        //开通saas需要同步创建组织，否则只创建客户，1：开通，0：不开通
        //不开通saas
        if(customerVo.getSaas().intValue()==0){
            customerVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            SequenceWorker worker = new SequenceWorker(0,0,0);
            customerVo.setId(String.valueOf(worker.nextId()));
            count = customerMapper.insert(customerVo);
            return count;
        }

            //开通saas
            // 同步添加组织
            AddOrgVo addOrgVo = new AddOrgVo();
            addOrgVo.setOrgName(customerVo.getName());
            addOrgVo.setContacts(customerVo.getName());
            addOrgVo.setManageAccount(customerVo.getPhoneNum().substring(customerVo.getPhoneNum().length()-11));
            addOrgVo.setManagePassword("rm123456");
            addOrgVo.setPhone(customerVo.getPhoneNum());
            // 第三方调用需加如下设置
            /**
             request.setAttribute("isNeedToken","false");
             addOrgVo.setSiteid(1L);
             **/
            ResultObject resultObject = customerFeignService.save(addOrgVo);
            if(ValidateUtil.isNotEmpty(resultObject) && resultObject.isSuccess()){
                // 组织添加成功再添加客户
                customerVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
                customerVo.setId(String.valueOf(resultObject.getData()));
                count = customerMapper.insert(customerVo);
            }else{
                log.error("添加客户同步添加组织信息失败:{}",resultObject);
                throw new MySelfValidateException(resultObject.getCode(),"同步组织失败，"+resultObject.getMsg());
            }

        return count;
    }



    /**
     * 普通插入
     * @param customerVo
     * @return
     */
    @Override
    @SetCreaterName
    @Transactional(rollbackFor = Exception.class)
    public int insert(CustomerVo customerVo) {
        // 判断客户编码是否存在
        CustomerVo customerVo1 =new CustomerVo();
        customerVo1.setCustomerCode(customerVo.getCustomerCode());
        customerVo1 = customerMapper.selectCustomerByCustomerCode(customerVo1);
        if(customerVo1!=null){
            log.warn("客户编码：{}，已存在",customerVo.getCustomerCode());
            throw new MySelfValidateException(ValidationEnum.CUSTOMER_CUSTOMER_CODE_EXISTS);
        }
        //判断客户名称是否存在
        customerVo1=new CustomerVo();
        customerVo1.setName(customerVo.getName());
        try{
            customerVo1=customerMapper.selectCustomerByCustomerName(customerVo1);
        }catch (Exception e){
            log.warn("客户名称数据异常：{}，客户名称重复",customerVo.getName());
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_CUSTOMERNAME_DUPLICATE);
        }
        if(customerVo1!=null){
            log.warn("客户名称：{}，已存在",customerVo.getName());
            throw new MySelfValidateException(ValidationEnum.CUSTOMER_CUSTOMER_NAME_EXISTS);
        }
       return insertCustomer(customerVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertupload(CustomerVo customerVo) {
        // 判断客户编码是否存在
        if(customerMapper.selectCustomerByCustomerCode(customerVo)!=null){
            log.warn("客户编码：{}，已存在",customerVo.getCustomerCode());
            throw new MySelfValidateException(ValidationEnum.CUSTOMER_CUSTOMER_CODE_EXISTS);
        }
        //判断客户名称是否存在
        if(customerMapper.selectCustomerByCustomerName(customerVo)!=null){
            log.warn("客户名称：{}，已存在",customerVo.getName());
            throw new MySelfValidateException(ValidationEnum.CUSTOMER_CUSTOMER_NAME_EXISTS);
        }
        customerVo.setStatus(1);
        int count=0;
        //开通saas需要同步创建组织，否则只创建客户，1：开通，0：不开通
        //不开通saas
        if(customerVo.getSaas().intValue()==0){
            customerVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            SequenceWorker worker = new SequenceWorker(0,0,0);
            customerVo.setId(String.valueOf(worker.nextId()));
            return ;
        }

        //开通saas
        // 同步添加组织
        AddOrgVo addOrgVo = new AddOrgVo();
        addOrgVo.setOrgName(customerVo.getName());
        addOrgVo.setContacts(customerVo.getName());
        addOrgVo.setManageAccount(customerVo.getPhoneNum().substring(customerVo.getPhoneNum().length()-11));
        addOrgVo.setManagePassword("5fd4b17bfc35c10e14c18460e38104df");
        addOrgVo.setPhone(customerVo.getPhoneNum());
        // 第三方调用需加如下设置
        /**
         request.setAttribute("isNeedToken","false");
         addOrgVo.setSiteid(1L);
         **/
        ResultObject resultObject = customerFeignService.save(addOrgVo);
        if(ValidateUtil.isNotEmpty(resultObject) && resultObject.isSuccess()){
            // 组织添加成功再添加客户
            customerVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            customerVo.setId(String.valueOf(resultObject.getData()));
        }else{
            log.error("添加客户同步添加组织信息失败:{}",resultObject);
            throw new MySelfValidateException(resultObject.getCode(),"同步组织失败，"+resultObject.getMsg());
        }
        return ;
    }


    /**
     * 批量插入
     * @param customerVos
     * @return
     */
    @Override
    @SetCreaterName
    public int insertList(List<CustomerVo> customerVos) {
        return customerMapper.insertList(customerVos);
    }

    /**
     * 更新
     * @param customerVo
     * @return
     */
    @Override
    public int update(CustomerVo customerVo) {
        customerVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        int count =0 ;
        //查看原本是否已开通saas
        ResultObject object = customerFeignService.select(Long.parseLong(customerVo.getId()));
        JSONObject json = JSONUtil.parseObj(object.getData());
        Integer orgStatus=json.getInt("status");
        //开通saas需要同步创建组织，否则只创建客户，1：开通，0：不开通
        //开通
        if((orgStatus==null||orgStatus!=1)&&customerVo.getSaas().intValue()==1){
             //首次开通
            if(orgStatus==null){
                // 同步添加组织
                AddOrgVo addOrgVo = new AddOrgVo();
                addOrgVo.setOrgName(customerVo.getName());
                addOrgVo.setManageAccount(customerVo.getPhoneNum().substring(customerVo.getPhoneNum().length()-11));
                addOrgVo.setManagePassword("5fd4b17bfc35c10e14c18460e38104df");
                addOrgVo.setPhone(customerVo.getPhoneNum());
                // 第三方调用需加如下设置
                /**
                 request.setAttribute("isNeedToken","false");
                 addOrgVo.setSiteid(1L);
                 **/
                ResultObject resultObject = customerFeignService.save(addOrgVo);
                if(ValidateUtil.isNotEmpty(resultObject) && resultObject.isSuccess()){
                    // 组织添加成功再修改客户
                    customerVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
                    customerVo.setOrgId(String.valueOf(resultObject.getData()));
                    customerMapper.updateId(customerVo);
                    customerVo.setId(customerVo.getOrgId());
                    count = customerMapper.update(customerVo);
                }else{
                    log.error("修改客户同步添加组织信息失败:{}",resultObject);
                    throw new MySelfValidateException(resultObject.getCode(),"同步组织失败，"+resultObject.getMsg());
                }
            }else if(orgStatus==0){
                //曾开通过再次开通
                ResultObject resultObject = customerFeignService.update(Integer.parseInt(customerVo.getId()));
                if(ValidateUtil.isNotEmpty(resultObject) && resultObject.isSuccess()){
                    // 组织添加成功再修改客户
                    customerVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
                    count = customerMapper.update(customerVo);
                }else{
                    log.error("修改客户同步添加组织信息失败:{}",resultObject);
                    throw new MySelfValidateException(resultObject.getCode(),"同步组织失败，"+resultObject.getMsg());
                }
            }
        }else if(orgStatus!=null&&orgStatus==1&&customerVo.getSaas().intValue()==0){
            //曾开通过不开通
            //删除组织
            ResultObject resultObject = customerFeignService.delete(Integer.parseInt(customerVo.getId()));
            if(ValidateUtil.isNotEmpty(resultObject) && resultObject.isSuccess()){
                customerVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
                count = customerMapper.update(customerVo);
            }else{
                log.error("修改客户同步删除组织信息失败:{}",resultObject);
                throw new MySelfValidateException(resultObject.getCode(),"同步组织失败，"+resultObject.getMsg());
            }
        }else{
            //已开通与不选择开通
            count = customerMapper.update(customerVo);
        }
        return count;
    }

    /**
     * 更新客户状态
     * @param customerVo
     * @return
     */
    @Override
    public int updateStatus(CustomerVo customerVo) {
        int count=0;
        //查看原本是否已开通saas
        ResultObject object = customerFeignService.select(Long.parseLong(customerVo.getId()));
        JSONObject json = JSONUtil.parseObj(object.getData());
        Integer orgStatus=json.getInt("status");
        if(customerVo.getStatus()==0){
            customerVo.setSaas(0);
            //如已开通saas,同步删除组织
            if(orgStatus!=null&&orgStatus==1){
                //删除组织
                ResultObject resultObject = customerFeignService.delete(Integer.parseInt(customerVo.getId()));
                if(!(ValidateUtil.isNotEmpty(resultObject) && resultObject.isSuccess())){
                    log.error("禁用客户同步删除组织信息失败:{}",resultObject);
                    throw new MySelfValidateException(resultObject.getCode(),"同步组织失败，"+resultObject.getMsg());
                }
            }
        }
        count = customerMapper.updateStatus(customerVo);
        return count;
    }

    /**
     * 分页查询
     * @param customerVo
     * @return
     */
    @Override
    public List<CustomerVo> select(Page<CustomerVo>page, CustomerVo customerVo) {
        return customerMapper.select(page,customerVo);

    }

    /**
     * 查询所有
     * @param customerVo
     * @return
     */
    @Override
    public List<CustomerVo> selectAll(CustomerVo customerVo) {
        return customerMapper.selectAll(customerVo);
    }


    /**
     * 查询所有有效的开通短报文的客户信息
     * @param customerVo
     * @return
     */
    @Override
    public List<CustomerVo> selectAllValidShortMsg(CustomerVo customerVo) {
        Set<String> customerCodes=new HashSet<>() ;
        //查询有效业务标识
        ServiceVo serviceVo=new ServiceVo();
        serviceVo.setCustomerName(customerVo.getName());
        List<ServiceVo> serviceVos = serviceMapper.selectEnableBusinewssByCustomerName(serviceVo);
        if(ValidateUtil.isNotEmpty(serviceVos)){
            for (ServiceVo serviceVo1:serviceVos) {
                if(serviceVo1.getServiceType()!=null&&serviceVo1.getServiceType().indexOf("5")!=-1){
                    customerCodes.add(serviceVo1.getCustomerCode());
                }
            }
        }
        List<CustomerVo> customerVos=new ArrayList<>();
        if(ValidateUtil.isNotEmpty(customerCodes)){
            customerVos=customerMapper.selectCustomerByCustomerCodes(customerCodes);
        }
        return customerVos;
    }

    /**
     * 根据终端号码分页查询
     * @param page
     * @param customerDto
     * @return
     */
    @Override
    public List<CustomerDto> selectByMsisdn(Page<?> page, CustomerDto customerDto) {
        return customerMapper.selectByMsisdn(page,customerDto);
    }

    @Override
    public List<CustomerVo> selectCommon(Page<?> page, CustomerDto customerDto) {
        TerminalCallbackVo terminalCallbackVo = new TerminalCallbackVo();
        terminalCallbackVo.setMsisdnList(customerDto.getMsisdnList());
        terminalCallbackVo.setApiCategory(customerDto.getApiCategory());
        terminalCallbackVo.setStatus(1);
        // 根据终端号码列表查询回调关系表service_id
        List<TerminalCallbackVo> list = terminalCallbackMapper.selectAll(terminalCallbackVo);

        List<String> serviceIds = list.stream().map(e->e.getServiceId()).collect(Collectors.toList());
        if(ValidateUtil.isEmpty(serviceIds)){
            return null;
        }
        // 返回回调地址等信息
        customerDto.setServiceIds(serviceIds);
        List<CustomerVo> customerVos = customerMapper.selectCommon(page,customerDto);
        List<CustomerVo> customerVos1 = new ArrayList<>();
        list.stream().forEach(terminalCallbackVo1 -> {
            if(ValidateUtil.isNotEmpty(terminalCallbackVo1.getServiceId())){
                CustomerVo customerVo1= new CustomerVo();
                CustomerVo customerVo = customerVos.stream().filter(e->e.getServiceId().equals(terminalCallbackVo1.getServiceId())).findFirst().orElse(null);
                if(ValidateUtil.isNotEmpty(customerVo)){
                    BeanUtils.copyProperties(customerVo,customerVo1);
                }
                customerVo1.setMsisdn(terminalCallbackVo1.getMsisdn());
                customerVos1.add(customerVo1);
            }
        });

        return customerVos1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response uploadCustomer(MultipartFile file, String createrId, HttpServletResponse httpServletResponse, HttpServletRequest request) {
        String msg="";
        String filename = file.getOriginalFilename();
        if(!filename.matches(FILENAMEREGEX)){
            log.error("批量导入模板文件名错误：{}！",filename);
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_CUSTOMER_NOTEXISTS);
        }
        ExcelReader excelReader = null;
        try {
            excelReader = ExcelUtil.getReader(file.getInputStream());
        } catch (IOException e) {
            log.error("客户信息导入异常:",e);
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_INPUTSTREAM_ERROR);
        }
        List<Map<String,Object>> readAll = excelReader.readAll();
        if(ValidateUtil.isEmpty(readAll)){
            log.error("导入的文件内容为空！");
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_EMPTY);
        }

        // 查询字典表
        DictVo dictVo = new DictVo();
        // 终端类型
        dictVo.setType("customer_industry");
        List<DictVo> industryTypeList = dictService.selectAll(dictVo);
        List<CustomerVo> list=new ArrayList<>();
        List<CustomerTenantVo> customerTenantVos=new ArrayList<>();
        Admin admin= (Admin) request.getSession().getAttribute("adminInfo");
        List<Map<String,Object>> errorList=new ArrayList<>();
        // TODO: 2020/12/31 如CustomerVo在外部进行创建对象 在每次for循环操作后实例化
        readAll.stream().forEach(map->{
            StringBuilder errorStr=new StringBuilder();
            CustomerVo customerVo = new CustomerVo();
            customerVo.setCreaterId(String.valueOf(admin.getAdminId()));
            customerVo.setCreaterName(String.valueOf(admin.getRealname()));
            customerVo.setTenantId(String.valueOf(admin.getSiteId()));
            customerVo.setOrgId(String.valueOf(admin.getOrgId()));
            //客户编码校验
            Object customerCode=map.get("客户编码");
            if(customerCode==null||customerCode.toString()==""||customerCode.toString().length()>20){
                log.warn("导入异常:客户编码格式校验不通过：{}！",customerCode);
                errorStr.append("|客户编码格式校验不通过");
            }else {
                customerVo.setCustomerCode(customerCode.toString());
            }
            //客户名称校验
            Object customerName=map.get("客户名称");
            if(customerName==null||customerName.toString()==""||customerName.toString().length()>100){
                log.warn("导入异常:客户名称格式校验不通过：{}！",customerName);
                errorStr.append("|客户名称格式校验不通过");
            }else {
                customerVo.setName(customerName.toString());
            }

            //IP地址校验
            Object ip=map.get("IP地址");
            if(ip==null||!ip.toString().matches(IPREGEX)){
                log.warn("导入异常:IP地址格式校验不通过：{}！",ip);
                errorStr.append("|IP地址格式校验不通过");
            }else{
                customerVo.setIp(ip.toString());
            }
            //客户号码校验
            Object phoneNum=map.get("客户号码");
            if(phoneNum==null||!phoneNum.toString().matches(PHONENUMREGEX)||phoneNum.toString().length()<11||phoneNum.toString().length()>21){
                log.warn("导入异常:客户号码格式校验不通过：{}！",phoneNum);
                errorStr.append("|客户号码格式校验不通过");
            }else {
                customerVo.setPhoneNum(phoneNum.toString());
            }
            //客户属性校验
            Object customerType=map.get("客户属性");
            if(customerType==null||customerType.toString()==""||customerType.toString().length()>20){
                log.warn("导入异常:客户属性格式校验不通过：{}！",customerType);
                errorStr.append("|客户属性格式校验不通过");
            }else {
                customerVo.setType(customerType.toString());
            }
            //客户权限校验
            Object limit = map.get("客户权限");
            if(limit==null||!limit.toString().equals("单次")&&!limit.toString().equals("周期")&&!limit.toString().equals("紧急定位")){
                log.warn("导入异常:客户权限格式校验不通过：{}！",limit);
                errorStr.append("|客户权限格式校验不通过");
            }else {
                Integer limitCode = "单次".equals(limit.toString())?3:"周期".equals(limit.toString())?1:2;
                customerVo.setLimit(limitCode);
            }

            //客户业务状态校验
            Object customerStatus = map.get("客户业务状态");
            if(customerStatus==null||!customerStatus.toString().equals("商用")&&!customerStatus.toString().equals("试商用")&&!customerStatus.toString().equals("测试")){
                log.warn("导入异常:客户业务状态格式校验不通过：{}！",customerStatus);
               errorStr.append("|客户业务状态格式校验不通过");
            }else {
                Integer code = "商用".equals(customerStatus.toString())?1:"试商用".equals(customerStatus.toString())?2:3;
                customerVo.setCustomerStatus(code);
            }

            //客户业务代码校验
            Object customerBusinessCode=map.get("客户业务代码");
            if(customerBusinessCode==null||customerBusinessCode.toString()==""||customerBusinessCode.toString().length()>20){
                log.warn("导入异常:客户业务代码格式校验不通过：{}！",customerBusinessCode);
                errorStr.append("|客户业务代码格式校验不通过");
            }else {
                customerVo.setCustomerBusinessCode(customerBusinessCode.toString());
            }

            //接入方式校验
            Object serviceWay=map.get("接入方式");
            if(serviceWay==null||serviceWay.toString()==""||serviceWay.toString().length()>21){
                log.warn("导入异常:接入方式格式校验不通过：{}！",serviceWay);
                errorStr.append("|接入方式格式校验不通过");
            }else {
                customerVo.setServiceWay(serviceWay.toString());
            }

            //业务优先级校验
            Object level = map.get("业务优先级");
            if(level==null||!level.toString().equals("高")&&!level.toString().equals("中")&&!level.toString().equals("低")){
                log.warn("业务优先级格式校验不通过：{}！",level);
                errorStr.append("|业务优先级格式校验不通过");
            }else {
                Integer levelCode = "高".equals(level.toString())?1:"中".equals(level)?2:3;
                customerVo.setLevel(levelCode);
            }

            //优先级顺序校验
           Object levelSeq=map.get("优先级顺序");
            if(levelSeq==null||levelSeq.toString()==""||levelSeq.toString().length()>21){
                log.warn("导入异常:优先级顺序格式校验不通过：{}！",levelSeq);
                errorStr.append("|优先级顺序格式校验不通过");
            }else {
                customerVo.setLevelSeq(levelSeq.toString());
            }
            DictVo dictVo1=null;
            //客户行业校验
            try {
                String industry = (String) map.get("客户行业");
                dictVo1 = industryTypeList.stream().filter(a->a.getDesc().equals(industry)).findFirst().get();
                customerVo.setIndustryList(dictVo1.getDesc());
            }catch (Exception e){
                log.warn("导入异常:客户行业格式校验不通过",e);
                errorStr.append("|客户行业格式校验不通过");
            }
            customerVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            customerVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            //开通校验
            Object saas=map.get("是否开通saas");
            if(saas==null||!saas.toString().equals("开通")&&!saas.toString().equals("不开通")){
                log.warn("导入异常:是否开通saas格式校验不通过：{}！",saas);
                errorStr.append("|是否开通saas格式校验不通过");
            }else {
                Integer saasCode="开通".equals(saas.toString())?1:0;
                customerVo.setSaas(saasCode);
            }
            //客户编码与客户名称存在但不一致的情况下不允许导入更新
            // TODO: 2020/12/31 可以SELECT ALL 在内存中对参数进行分析组装 如果对内存影响过大可以分段select(使用Limit)
            CustomerVo customerVoByCodeAndByName=customerMapper.selectCustomerByCustomerCodeAndCustomerName(customerVo);
            if(customerVoByCodeAndByName!=null){
                //更新
                if(customerVoByCodeAndByName.getStatus()==0){
                    log.warn("导入异常:客户状态为无效：{}！",customerVoByCodeAndByName.getId());
                    errorStr.append("|客户状态为无效");
                }else if(errorStr.length()==0){
                    customerVo.setId(customerVoByCodeAndByName.getId());
                    customerVo.setStatus(1);
                    try {
                        // TODO: 2020/12/31 自行构建批量更新方法 每1000进行分割
                        update(customerVo);
                    }catch (MySelfValidateException e){
                        errorStr.append("|"+e.getValue());
                    }
                }
            }else {
                //新增
                if(errorStr.length()==0){
                    try {
                        // TODO: 2020/12/31 自行构建批量插入方法 每1000进行分割
                        insertupload(customerVo);
                    }catch (MySelfValidateException e){
                        errorStr.append("|"+e.getValue());
                    }
                }
                if(errorStr.length()==0){
                    //同步客户与租户关系表
                    CustomerTenantVo customerTenantVo=new CustomerTenantVo();
                    customerTenantVo.setTenantId(customerVo.getTenantId());
                    customerTenantVo.setCustomerCode(customerVo.getCustomerCode());
                    customerTenantVos.add(customerTenantVo);
                    list.add(customerVo);
                }
            }
            if(errorStr.length()>0){
                map.put("异常信息",errorStr.substring(1));
                errorList.add(map);
            }
        });
        if(ValidateUtil.isNotEmpty(list)&&ValidateUtil.isNotEmpty(customerTenantVos)){
            // TODO: 2020/12/31 实现批量插入，每1000条分批 
            customerTenantMapper.insertList(customerTenantVos);
            customerMapper.insertList(list);
        }
        msg="共导入"+readAll.size()+"条，导入成功"+(readAll.size()-errorList.size())+"条，失败"+errorList.size()+"条";
        if(ValidateUtil.isEmpty(errorList)){
            return new Response(1,"成功",msg);
        }
        //将异常信息暂存在数据库
        UpLoadErrorPo upLoadErrorPo=new UpLoadErrorPo();
        String key = UUID.randomUUID().toString();
        upLoadErrorPo.setKey(key);
        upLoadErrorPo.setKeyText(JSON.toJSONString(errorList));
        uploadErrorMapper.insert(upLoadErrorPo);
        return new Response(0,key,msg);
    }

    @Override
    public void export(CustomerVo customerVo, HttpServletResponse response) {
        //GET请求会将+转化为空格,处理客户号码
        customerVo.setPhoneNum(customerVo.getPhoneNum().replace(' ','+'));
        List<CustomerVo> list = selectAll(customerVo);
        if(ValidateUtil.isNotEmpty(list)){
            String str1="1";
            String str2="2";
            list.stream().forEach(customerVo1 -> {
                if(customerVo1.getLimit()==Integer.parseInt(str1)){
                    customerVo1.setLimitStr("周期");
                }else if(customerVo1.getLimit()==Integer.parseInt(str2)){
                    customerVo1.setLimitStr("紧急定位");
                }else{
                    customerVo1.setLimitStr("单次");
                }

                if(customerVo1.getCustomerStatus()==Integer.parseInt(str1)){
                    customerVo1.setCustomerStatusStr("商用");
                }else if(customerVo1.getCustomerStatus()==Integer.parseInt(str2)){
                    customerVo1.setCustomerStatusStr("试商用");
                }else{
                    customerVo1.setCustomerStatusStr("测试");
                }


                if(customerVo1.getLevel()==Integer.parseInt(str1)){
                    customerVo1.setLevelStr("高");
                }else if(customerVo1.getLevel()==Integer.parseInt(str2)){
                    customerVo1.setLevelStr("中");
                }else{
                    customerVo1.setLevelStr("低");
                }

                if(customerVo1.getStatus()==Integer.parseInt(str1)){
                    customerVo1.setStatusStr("有效");
                }else{
                    customerVo1.setStatusStr("无效");
                }

            });
        }
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        // 设置列宽
        writer.setColumnWidth(0,30);
        writer.setColumnWidth(1,30);
        writer.setColumnWidth(2,30);
        // 必须设置，否则多首列
        writer.setOnlyAlias(true);
        // 修改别名
        writer.addHeaderAlias("customerCode","客户编码");
        writer.addHeaderAlias("name","客户名称");
        writer.addHeaderAlias("ip","IP地址");
        writer.addHeaderAlias("phoneNum","客户号码");
        writer.addHeaderAlias("type","客户属性");
        writer.addHeaderAlias("limitStr","客户权限");
        writer.addHeaderAlias("customerStatusStr","客户业务状态");
        writer.addHeaderAlias("customerBusinessCode","客户业务代码");
        writer.addHeaderAlias("serviceWay","接入方式");
        writer.addHeaderAlias("levelStr","业务优先级");
        writer.addHeaderAlias("levelSeq","优先级顺序");
        writer.addHeaderAlias("industryList","客户行业");
        writer.addHeaderAlias("StatusStr","客户状态");
        writer.write(list);

        //customer.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=customer.xls");
        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            log.error("客户信息导出异常：",e);
            throw new MySelfValidateException(ValidationEnum.EXPORTFILE_OUTPUTSTREAM_ERROR);
        }finally {
            writer.flush(out);
            // 关闭writer，释放内存
            writer.close();
        }
    }


    /**
     * 导出错误记录及异常提示信息
     * @param key
     * @param response
     */
    @Override
    public void exportError(String key, HttpServletResponse response) {
        UpLoadErrorPo upLoadErrorPo=uploadErrorMapper.selectByKey(key);
        List<Map> errorList= null;
        try {
            errorList = JsonUtil.json2Object(upLoadErrorPo.getKeyText(), List.class);
        } catch (JsonProcessingException e) {
            log.error("{}jsonString转对象失败",this.getClass().getSimpleName());
        }
        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter();
        // 设置列宽
        writer.setColumnWidth(0,30);
        writer.setColumnWidth(1,30);
        writer.setColumnWidth(2,30);
        writer.setColumnWidth(3,30);
        writer.setColumnWidth(4,30);
        writer.setColumnWidth(5,30);
        writer.setColumnWidth(6,30);
        writer.setColumnWidth(7,30);
        writer.setColumnWidth(8,30);
        writer.setColumnWidth(9,30);
        writer.setColumnWidth(10,30);
        writer.setColumnWidth(11,30);
        writer.setColumnWidth(12,30);
        writer.setColumnWidth(13,100);
        // 必须设置，否则多首列
        writer.setOnlyAlias(true);
        writer.write(errorList);
        //customer.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=customererror.xls");
        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            log.error("客户导出异常：",e);
            throw new MySelfValidateException(ValidationEnum.EXPORTFILE_OUTPUTSTREAM_ERROR);
        }finally {
            // 关闭writer，释放内存
            writer.flush(out);
            writer.close();
        }
    }
}
