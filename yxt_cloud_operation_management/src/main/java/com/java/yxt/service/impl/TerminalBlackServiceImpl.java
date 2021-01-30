package com.java.yxt.service.impl;

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
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.po.ProductionPo;
import com.java.yxt.po.UpLoadErrorPo;
import com.java.yxt.service.DictService;
import com.java.yxt.service.TerminalBlackService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * TerminalBlackServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description &lt;文件描述&gt;
 * @Package com.java.yxt.service.impl
 * @date 2020/9/19
 */
@Service
@Slf4j
public class TerminalBlackServiceImpl implements TerminalBlackService {

    @Autowired
    TerminalBlackMapper terminalBlackMapper;
    @Autowired
    RedissonClient redissonUtils;
    @Autowired
    TerminalMapper terminalMapper;
    @Autowired
    DictService dictService;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    ServiceMapper serviceMapper;
    @Autowired
    ProductionMapper productionMapper;
    @Autowired
    UploadErrorMapper uploadErrorMapper;
    //校验终端用户导入文件名正则
    public static final String FILENAME="^terminalblack{1}.*\\.xls{1}$";
    //校验MSISDN正则
    public static final String MSISDNREGEX="^\\+{0,1}[0-9]+\\-{0,1}[0-9]+$";
    /**
     * 插入
     * @param terminalBlackVo
     * @return
     */
    @Override
    @SetCreaterName
    public int insert(TerminalBlackVo terminalBlackVo) {
        terminalBlackVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        // sp客户黑名单
        String type = "customer";
        //查询客户是否存在，不存在时无法添加
        if(terminalBlackVo.getCustomerCode()==null){
            log.error("导入异常:客户名称有误或不存在");
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_CUSTOMERCODE_NOTEXISTS);
        }
        if(terminalBlackVo.getLevel().equals(type)){
            // 查询客户名称，业务标识,主被叫类型是否存在，存在则更新
            TerminalBlackVo terminalBlackVos = terminalBlackMapper.isTerminalSpExists(terminalBlackVo);
            if(ValidateUtil.isNotEmpty(terminalBlackVos)){
                log.warn("客户名称:{},业务标识:{}，主被叫类型:{},已存在，请更新。",terminalBlackVo.getCustomerName(),terminalBlackVo.getServiceCode(),terminalBlackVo.getType());
                throw new MySelfValidateException(ValidationEnum.BLACK_SP_UNIQUE_EXISTS);
            }
        }else{
            // 查询限制类型，终端id,主被叫类型是否存在，存在则更新
            List<TerminalBlackVo> terminalBlackVos = terminalBlackMapper.isTerminalExists(terminalBlackVo);
            if(ValidateUtil.isNotEmpty(terminalBlackVos) && terminalBlackVos.size()>0){
                log.warn("msisdn:{},限制等级：{}，主被叫类型：{} 已存在，请更新。",terminalBlackVo.getMsisdn(),terminalBlackVo.getLevel(),terminalBlackVo.getType());
                throw new MySelfValidateException(ValidationEnum.BLACK_TERMINAL_UNIQUE_EXISTS);
            }
        }
        return terminalBlackMapper.insert(terminalBlackVo);
    }

    public void insertupload(TerminalBlackVo terminalBlackVo) {
        terminalBlackVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        // sp客户黑名单
        String type = "customer";
        //查询客户是否存在，不存在时无法添加
        if(terminalBlackVo.getCustomerCode()==null){
            log.error("导入异常:客户名称有误或不存在");
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_CUSTOMERCODE_NOTEXISTS);
        }
        if(terminalBlackVo.getLevel().equals(type)){
            // 查询客户名称，业务标识,主被叫类型是否存在，存在则更新
            TerminalBlackVo terminalBlackVos = terminalBlackMapper.isTerminalSpExists(terminalBlackVo);
            if(ValidateUtil.isNotEmpty(terminalBlackVos)){
                log.warn("客户名称:{},业务标识:{}，主被叫类型:{},已存在，请更新。",terminalBlackVo.getCustomerName(),terminalBlackVo.getServiceCode(),terminalBlackVo.getType());
                throw new MySelfValidateException(ValidationEnum.BLACK_SP_UNIQUE_EXISTS);
            }
        }else{
            // 查询限制类型，终端id,主被叫类型是否存在，存在则更新
            List<TerminalBlackVo> terminalBlackVos = terminalBlackMapper.isTerminalExists(terminalBlackVo);
            if(ValidateUtil.isNotEmpty(terminalBlackVos) && terminalBlackVos.size()>0){
                log.warn("msisdn:{},限制等级：{}，主被叫类型：{} 已存在，请更新。",terminalBlackVo.getMsisdn(),terminalBlackVo.getLevel(),terminalBlackVo.getType());
                throw new MySelfValidateException(ValidationEnum.BLACK_TERMINAL_UNIQUE_EXISTS);
            }
        }
    }

    /**
     * 更新终端用户
     * @param terminalBlackVo
     * @return
     */
    @Override
    public int updateUser(TerminalBlackVo terminalBlackVo) {
        // 查询是否已存在信息
        TerminalBlackVo terminalBlackVo1 = new TerminalBlackVo();
//        terminalBlackVo1.setId(terminalBlackVo.getId());
        terminalBlackVo1.setTerminalId(terminalBlackVo.getTerminalId());
        terminalBlackVo1.setLevel(terminalBlackVo.getLevel());
        terminalBlackVo1.setType(terminalBlackVo.getType());

        List<TerminalBlackVo> terminalBlackVos = terminalBlackMapper.isTerminalExists(terminalBlackVo1);
        if(ValidateUtil.isNotEmpty(terminalBlackVos) && terminalBlackVos.size()>0&&!terminalBlackVos.get(0).getId().equals(terminalBlackVo.getId())){
            log.error("更改终端黑名单失败，msisdn:{},主被叫类型：{}，限制级别：{} 已存在",terminalBlackVo1.getMsisdn(),
                    terminalBlackVo1.getType(),terminalBlackVo1.getLevel());
            throw  new MySelfValidateException(ValidationEnum.BLACK_TERMINAL_UNIQUE_EXISTS);
        }
        terminalBlackVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return terminalBlackMapper.update(terminalBlackVo);
    }


    /**
     * 更新SP应用平台
     * @param terminalBlackVo
     * @return
     */
    @Override
    public int updateSp(TerminalBlackVo terminalBlackVo) {// 查询是否已存在信息
        //查找所有未关联的业务标识
        List<String> serviceCodes=new ArrayList<>();
        List<ServiceVo> list=serviceMapper.unRelationBlackServiceCode(terminalBlackVo);
        for (ServiceVo s:list) {
            serviceCodes.add(s.getServiceCode());
        }
        TerminalBlackVo terminalBlackVo1=terminalBlackMapper.selectTerminalBlackByContion(terminalBlackVo);
        if (!serviceCodes.contains(terminalBlackVo.getServiceCode())&&terminalBlackVo1!=null&&!terminalBlackVo.getId().equals(terminalBlackVo1.getId())){
            log.error("业务标识不存在或已被添加：{}！",terminalBlackVo.getServiceCode());
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_SERVICECODE_ERROR);
        }

        terminalBlackVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return terminalBlackMapper.update(terminalBlackVo);
    }

    /**
     * 分页查询终端用户
     * @param page
     * @param terminalBlackVo
     * @return
     */
    @Override
    public List<TerminalBlackVo> selectUser(Page<?> page, TerminalBlackVo terminalBlackVo) {
    	return terminalBlackMapper.selectUser(page,terminalBlackVo);
    }

    /**
     * 分页查询SP用户
     * @param page
     * @param terminalBlackVo
     * @return
     */
    @Override
    public List<TerminalBlackVo> selectSp(Page<?> page, TerminalBlackVo terminalBlackVo) {
        return terminalBlackMapper.selectSp(page,terminalBlackVo);
    }
    /**
     * 不分页查询
     * @param terminalBlackVo
     * @return
     */
    @Override
    public List<TerminalBlackVo> selectAll(TerminalBlackVo terminalBlackVo) {
        return terminalBlackMapper.selectAll(terminalBlackVo);
    }

    /**
     * 启用禁用
     * @param terminalBlackVo
     * @return
     */
    @Override
    public int enableDisable(TerminalBlackVo terminalBlackVo) {
        return terminalBlackMapper.enableDisable(terminalBlackVo);
    }


    /**
     * 导入主被叫黑名单-终端用户
     * @return void
     * @param file
     * @param createrId
     * @return List<String>
     */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public Response uploadTerminalBlackUser(MultipartFile file, String createrId, HttpServletResponse httpServletResponse, HttpServletRequest request) {
        String msg="";
	    String filename = file.getOriginalFilename();
        if(!filename.matches(FILENAME)){
	            log.error("批量导入模板文件名错误：{}！",filename);
	            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_TERMINALBLACK_NOTEXISTS);
	        }
	        ExcelReader excelReader = null;
	        try {
	            excelReader = ExcelUtil.getReader(file.getInputStream());
	        } catch (IOException e) {
	            log.error("终端黑名单导入异常:",e);
	            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_INPUTSTREAM_ERROR);
	        }
	        List<Map<String,Object>> readAll = excelReader.readAll();
	        if(ValidateUtil.isEmpty(readAll)){
	            log.error("导入的文件内容为空！");
	            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_EMPTY);
	        }
	        List<TerminalBlackVo> userList=new ArrayList<>();
            List<Map<String,Object>> errorList=new ArrayList<>();
            Admin admin= (Admin) request.getSession().getAttribute("adminInfo");
	        readAll.stream().forEach(map->{
                StringBuilder errorStr=new StringBuilder();
                TerminalBlackVo terminalBlackVo = new TerminalBlackVo();
                terminalBlackVo.setCreaterId(String.valueOf(admin.getAdminId()));
                terminalBlackVo.setCreaterName(String.valueOf(admin.getRealname()));
                terminalBlackVo.setTenantId(String.valueOf(admin.getSiteId()));
                terminalBlackVo.setOrgId(String.valueOf(admin.getOrgId()));
                TerminalVo terminalVo=new TerminalVo();
                //MSISDN校验
                Object msisdn=map.get("MSISDN");
                if(msisdn==null||!msisdn.toString().matches(MSISDNREGEX)||msisdn.toString().length()>21){
                    log.warn("导入异常:MSISDN格式校验不通过");
                    errorStr.append("|MSISDN格式校验不通过");
                }else {
                    terminalVo.setMsisdn(msisdn.toString());
                    try{
                        terminalVo=terminalMapper.selectterminalbymsisdn(terminalVo);
                    }catch (Exception e){
                        log.warn("导入异常:MSISDN数据异常",e);
                        errorStr.append("|MSISDN数据异常");
                    }
                    if(terminalVo==null){
                        log.warn("导入异常:MSISDN用户不存在或状态不正常");
                        errorStr.append("|MSISDN用户不存在或状态不正常");
                    }
                    if(errorStr.length()==0){
                        terminalBlackVo.setMsisdn(msisdn.toString());
                    }
                    //客户名称校验
                    Object customerName=map.get("客户名称");
                    if(customerName==null||customerName.toString()==""||customerName.toString().length()>100){
                        log.warn("导入异常:客户名称格式校验不通过！");
                        errorStr.append("|客户名称格式校验不通过");
                    }else if(terminalVo!=null){
                        terminalVo.setCustomerName(customerName.toString());
                        CustomerVo customerVo=new CustomerVo();
                        customerVo.setName(customerName.toString());
                        customerVo=customerMapper.selectCustomerByCustomerName(customerVo);
                        if(customerVo==null||customerVo.getStatus()==0){
                            log.warn("导入异常:客户不存在或客户状态无效！");
                            errorStr.append("|客户不存在或客户状态无效");
                        }
                        if(errorStr.length()==0){
                            terminalBlackVo.setCustomerName(customerName.toString());
                        }
                        if(terminalBlackVo.getMsisdn()!=null&&terminalBlackVo.getCustomerName()!=null){
                            //客户与用户绑定关系校验
                            TerminalVo terminalVo1=terminalMapper.selectterminalbymsisdnandcustomername(terminalVo);
                            if(terminalVo1==null){
                                log.warn("导入异常:客户与用户绑定关系不存在");
                                errorStr.append("|客户与用户绑定关系不存在");
                            }else {
                                terminalVo=terminalVo1;
                                terminalBlackVo.setStatus(1);
                                terminalBlackVo.setTerminalId(terminalVo.getId());
                                terminalBlackVo.setCustomerCode(terminalVo.getCustomerCode());
                            }
                        }
                    }
                }
                //限制等级校验
	            Object level=map.get("限制等级");
	            if(level==null||!level.toString().equals("用户系统级")&&!level.toString().equals("用户行业级")){
                    log.warn("导入异常:限制等级格式校验不通过！");
                    errorStr.append("|限制等级格式校验不通过");
                }else {
                    terminalBlackVo.setLevel("用户系统级".equals(level.toString())?"terminal_sys":"terminal_idt");
                }
	            //主被叫类型校验
	            Object typeStr=map.get("主被叫类型");
	            if(typeStr==null||!typeStr.toString().equals("主叫")&&!typeStr.toString().equals("被叫")){
                    log.warn("导入异常:主被叫类型格式校验不通过！");
                    errorStr.append("|主被叫类型格式校验不通过");
                }else {
                    terminalBlackVo.setType("主叫".equals(typeStr.toString())?"MO":"MT");
                }
                if(errorStr.length()==0){
                    try {
                        insertupload(terminalBlackVo);
                    }catch (MySelfValidateException e){
                        errorStr.append("|"+e.getValue());
                    }
                    if(errorStr.length()==0){
                        userList.add(terminalBlackVo);
                    }
                }
                if(errorStr.length()>0){
                    map.put("异常信息",errorStr.substring(1));
                    errorList.add(map);
                }
	        });
	        if(ValidateUtil.isNotEmpty(userList)){
	            terminalBlackMapper.insertList(userList);
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


    /**
     * 导入主被叫黑名单-SP应用平台
     * @return void
     * @param file
     * @param createrId
     * @return Response
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response  uploadTerminalBlackSp(MultipartFile file, String createrId,HttpServletResponse httpServletResponse,HttpServletRequest request) {
        String msg="";
        String filename = file.getOriginalFilename();
        if(!filename.matches(FILENAME)){
            log.error("批量导入模板文件名错误：{}！",filename);
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_TERMINALBLACK_NOTEXISTS);
        }
        ExcelReader excelReader = null;
        try {
            excelReader = ExcelUtil.getReader(file.getInputStream());
        } catch (IOException e) {
            log.error("终端黑名单导入异常:",e);
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_INPUTSTREAM_ERROR);
        }
        List<Map<String,Object>> readAll = excelReader.readAll();
        if(ValidateUtil.isEmpty(readAll)){
            log.error("导入的文件内容为空！");
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_EMPTY);
        }
        List<TerminalBlackVo> spList=new ArrayList<>();
        List<Map<String,Object>> errorList=new ArrayList<>();
        Admin admin= (Admin) request.getSession().getAttribute("adminInfo");
        readAll.stream().forEach(map->{
            String errorStr="";
            TerminalBlackVo terminalBlackVo = new TerminalBlackVo();
            terminalBlackVo.setCreaterId(String.valueOf(admin.getAdminId()));
            terminalBlackVo.setCreaterName(String.valueOf(admin.getRealname()));
            terminalBlackVo.setTenantId(String.valueOf(admin.getSiteId()));
            terminalBlackVo.setOrgId(String.valueOf(admin.getOrgId()));
            //客户名称校验
            Object customerName=map.get("客户名称");
            if(customerName==null||customerName.toString()==""||customerName.toString().length()>100){
                log.warn("导入异常:客户名称格式校验不通过：{}！",customerName);
                errorStr+="|客户名称格式校验不通过";
            }else {
                terminalBlackVo.setCustomerName(customerName.toString());
                terminalBlackVo.setLevel("customer");
            }
            if(map.get("业务标识")==null){
                log.warn("导入异常:业务标识格式校验不通过！");
                errorStr+="|业务标识格式校验不通过";
                }else {
                terminalBlackVo.setServiceCode(map.get("业务标识").toString());
            }

            //主被叫类型校验
            Object typeStr=map.get("主被叫类型");
            if(typeStr==null||!typeStr.toString().equals("主叫")&&!typeStr.toString().equals("被叫")){
                log.warn("导入异常:主被叫类型格式校验不通过：{}！",typeStr);
                errorStr+="|主被叫类型格式校验不通过";
            }else {
                terminalBlackVo.setType("主叫".equals(typeStr.toString())?"MO":"MT");
                terminalBlackVo.setStatus(1);
            }
            if(terminalBlackVo.getCustomerName()!=null){
                CustomerVo customerVo=new CustomerVo();
                customerVo.setName(terminalBlackVo.getCustomerName());
                customerVo=customerMapper.selectCustomerByCustomerName(customerVo);
                if(customerVo==null||customerVo.getStatus()==0){
                    log.warn("导入异常:客户不存在或客户状态无效！");
                    errorStr+="|客户不存在或客户状态无效";
                }else {
                    terminalBlackVo.setCustomerCode(customerVo.getCustomerCode());
                    terminalBlackVo.setCustomerId(customerVo.getId());
                    //查找所有未关联的业务标识
                    List<ServiceVo> list=serviceMapper.unRelationBlackServiceCode(terminalBlackVo);
                    List<String> serviceCodes=list.stream().map(s->s.getServiceCode()).collect(Collectors.toList());
                    if (!serviceCodes.contains(terminalBlackVo.getServiceCode())){
                        log.error("业务标识不存在或已被添加：{}！",terminalBlackVo.getServiceCode());
                        errorStr+="|业务标识不存在或已被添加";
                    }
                }
            }
            if(errorStr.length()==0){
                try {
                    //查看销售品状态，无效时不允许添加
                    ServiceVo serviceVo=new ServiceVo();
                    serviceVo.setServiceCode(terminalBlackVo.getServiceCode());
                    List<ServiceVo> list=serviceMapper.selectAll(serviceVo);
                    serviceVo=list.get(0);
                    ProductionVo productionVo=new ProductionVo();
                    productionVo.setSaleCode(serviceVo.getProductId());
                    ProductionPo productionPo=productionMapper.selectBySaleCode(productionVo);
                    if(productionPo.getStatus()==0){
                        log.error("主被叫黑名单SP应用平台添加异常：销售品状态无效！");
                        throw new MySelfValidateException(ValidationEnum.PRODUCTION_STATUS_ERROR);
                    }
                    terminalBlackVo.setLevel("customer");
                    insertupload(terminalBlackVo);
                }catch (MySelfValidateException e){
                    errorStr+=("|"+e.getValue());
                }
                if(errorStr.length()==0){
                    spList.add(terminalBlackVo);
                }
            }
            if(errorStr.length()>0){
                map.put("异常信息",errorStr.substring(1));
                errorList.add(map);
            }
        });
        if(ValidateUtil.isNotEmpty(spList)){
            terminalBlackMapper.insertList(spList);
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


    /**
     * 插入终端用户
     * @param terminalBlackVo
     * @return
     */
    @Override
    public void insertUser(TerminalBlackVo terminalBlackVo) {
        //查看MSISDN终端用户状态，非正常时不允许添加
        TerminalVo terminalVo=new TerminalVo();
        terminalVo.setId(terminalBlackVo.getTerminalId());
        terminalVo=terminalMapper.selectAll(terminalVo).get(0);
        if(terminalVo.getUserStatus()!=1){
            log.error("主被叫黑名单终端用户添加异常:用户状态不正常！");
            throw new MySelfValidateException(ValidationEnum.TERMINAL_USER_STATUS_INVALID);
        }
        //查看客户状态,无效时不允许添加
        CustomerVo customerVo=new CustomerVo();
        customerVo.setId(terminalBlackVo.getCustomerId());
        customerVo=customerMapper.selectAll(customerVo).get(0);
        if(customerVo.getStatus()==0){
            log.error("主被叫黑名单终端用户添加异常：客户状态无效！");
            throw new MySelfValidateException(ValidationEnum.CUSTOMER_CUSTOMER_STATUS_INVALID);
        }
        insert(terminalBlackVo);
    }

    /**
     * 插入SP应用平台用户
     * @param terminalBlackVo
     * @return
     */
    @Override
    public void insertSp(TerminalBlackVo terminalBlackVo) {
        //查看销售品状态，无效时不允许添加
        ServiceVo serviceVo=new ServiceVo();
        serviceVo.setServiceCode(terminalBlackVo.getServiceCode());
        List<ServiceVo> list=serviceMapper.selectAll(serviceVo);
        serviceVo=list.get(0);
        ProductionVo productionVo=new ProductionVo();
        productionVo.setSaleCode(serviceVo.getProductId());
        ProductionPo productionPo=productionMapper.selectBySaleCode(productionVo);
        if(productionPo.getStatus()==0){
            log.error("主被叫黑名单SP应用平台添加异常：销售品状态无效！");
            throw new MySelfValidateException(ValidationEnum.PRODUCTION_STATUS_ERROR);
        }
        terminalBlackVo.setLevel("customer");
        insert(terminalBlackVo);
    }

    /**
     * 终端用户导出记录
     * @param terminalBlackVo
     * @param response
     */
    @Override
    public void exportUser(TerminalBlackVo terminalBlackVo, HttpServletResponse response) {
        //GET请求会将+转化为空格,处理msisdn
        terminalBlackVo.setMsisdn(terminalBlackVo.getMsisdn().replace(' ','+'));
        List<TerminalBlackVo> list = selectUser(null,terminalBlackVo);
        if(ValidateUtil.isNotEmpty(list)){
            String str1="1";
            String str2="2";
            list.stream().forEach(terminalBlackVo1 -> {
                if("MO".equals(terminalBlackVo1.getType())){
                    terminalBlackVo1.setType("主叫");
                }else{
                    terminalBlackVo1.setType("被叫");
                }

                if("terminal_sys".equals(terminalBlackVo1.getLevel())){
                    terminalBlackVo1.setLevel("用户系统级");
                }else if("terminal_idt".equals(terminalBlackVo1.getLevel())){
                    terminalBlackVo1.setLevel("用户行业级");
                }else{
                    terminalBlackVo1.setLevel("客户级");
                }


                if(terminalBlackVo1.getStatus()==Integer.parseInt(str1)){
                    terminalBlackVo1.setStatusStr("有效");
                }else{
                    terminalBlackVo1.setStatusStr("无效");
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
        writer.addHeaderAlias("msisdn","MSISDN");
        writer.addHeaderAlias("customerName","客户名称");
        writer.addHeaderAlias("type","主被叫类型");
        writer.addHeaderAlias("level","限制等级");
        writer.addHeaderAlias("statusStr","黑名单状态");
        writer.addHeaderAlias("createrName","创建人");
        writer.addHeaderAlias("createTime","创建时间");
        writer.write(list);

        //terminalwhite.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=terminalblackuser.xls");
        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            log.error("终端用户黑名单导出异常：",e);
            throw new MySelfValidateException(ValidationEnum.EXPORTFILE_OUTPUTSTREAM_ERROR);
        }finally {
            writer.flush(out);
            // 关闭writer，释放内存
            writer.close();
        }

    }


    /**
     * SP平台导出记录
     * @param terminalBlackVo
     * @param response
     */
    @Override
    public void exportSp(TerminalBlackVo terminalBlackVo, HttpServletResponse response) {
        List<TerminalBlackVo> list = selectSp(null,terminalBlackVo);
        if(ValidateUtil.isNotEmpty(list)){
            String str1="1";
            String str2="2";
            list.stream().forEach(terminalBlackVo1 -> {
                if("MO".equals(terminalBlackVo1.getType())){
                    terminalBlackVo1.setType("主叫");
                }else{
                    terminalBlackVo1.setType("被叫");
                }
                if(terminalBlackVo1.getStatus()==Integer.parseInt(str1)){
                    terminalBlackVo1.setStatusStr("有效");
                }else{
                    terminalBlackVo1.setStatusStr("无效");
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
        writer.addHeaderAlias("customerName","客户名称");
        writer.addHeaderAlias("serviceCode","业务标识");
        writer.addHeaderAlias("type","主被叫类型");
        writer.addHeaderAlias("statusStr","黑名单状态");
        writer.addHeaderAlias("createrName","创建人");
        writer.addHeaderAlias("createTime","创建时间");
        writer.write(list);

        //terminalwhite.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=terminalblacksp.xls");
        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            log.error("终端用户黑名单导出异常：",e);
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
    public void exportUserError(String key, HttpServletResponse response) {
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
        writer.setColumnWidth(3,100);
        // 必须设置，否则多首列
        writer.setOnlyAlias(true);
        writer.write(errorList);
        //customer.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=terminalblackerror.xls");
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

    /**
     * 导出错误记录及异常提示信息
     * @param key
     * @param response
     */
    @Override
    public void exportSpError(String key, HttpServletResponse response) {
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
        writer.setColumnWidth(3,100);
        // 必须设置，否则多首列
        writer.setOnlyAlias(true);
        writer.write(errorList);
        //customer.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=terminalblackerror.xls");
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

    /**
     * SP应用平台查询所有客户名称
     * @param terminalBlackVo
     * @return
     */
    @Override
    public List<TerminalBlackVo> selectByCustomerName(TerminalBlackVo terminalBlackVo) {
        return terminalBlackMapper.selectByCustomerName(terminalBlackVo);
    }

    /**
     * SP应用平台查询有效业务标识
     * @param terminalBlackVo
     * @return
     */
    @Override
    public List<ServiceVo> selectServiceCode(TerminalBlackVo terminalBlackVo) {
        return serviceMapper.unRelationBlackServiceCode(terminalBlackVo);
    }
}
