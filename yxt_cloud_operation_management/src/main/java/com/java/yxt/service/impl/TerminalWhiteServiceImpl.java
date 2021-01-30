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
import com.java.yxt.constant.RedisKeyConst;
import com.java.yxt.dao.*;
import com.java.yxt.dto.ServiceCodeMsisdnsDto;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.po.UpLoadErrorPo;
import com.java.yxt.service.*;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
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
 * TerminalWhiteServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description &lt;文件描述&gt;
 * @Package com.java.yxt.service.impl
 * @date 2020/9/19
 */
@Service
@Slf4j
public class TerminalWhiteServiceImpl implements TerminalWhiteService {
    @Autowired
    TerminalWhiteMapper terminalWhiteMapper;

    @Autowired
    TerminalBlackMapper terminalBlackMapper;

    @Autowired
    RedissonClient redissonUtils;

    @Autowired
    BusinessService businessService;

    @Autowired
    ProductionApiRelationService productionApiRelationService;

    @Autowired
    TerminalCallbackService terminalCallbackService;

    @Autowired
    TerminalService terminalService;

    @Autowired
    TerminalCallbackMapper terminalCallbackMapper;

    @Autowired
    DictService dictService;
    
    @Autowired
    TerminalMapper terminalMapper;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    ServiceMapper serviceMapper;

    @Autowired
    UploadErrorMapper uploadErrorMapper;
    //校验导入文件名正则
    public static final String FILENAMEREGEX="^terminalwhite{1}.*\\.xls{1}$";
    //校验MSISDN正则
    public static final String MSISDNREGEX="^\\+{0,1}[0-9]+\\-{0,1}[0-9]+$";
    /**
     * 插入
     * @param terminalWhiteVo
     * @return
     */
    @Override
    @SetCreaterName
    @Transactional(rollbackFor = Exception.class)
    public int insert(TerminalWhiteVo terminalWhiteVo) {

        String msisdn = terminalWhiteVo.getMsisdn();
        String callbackUrl = "";

        //查看MSISDN终端用户状态，非正常时不允许添加
        TerminalVo terminalVo=new TerminalVo();
        terminalVo.setMsisdn(msisdn);
        terminalVo=terminalMapper.selectterminalbymsisdn(terminalVo);
        if(terminalVo==null){
            log.error("添加白名单，用户终端状态不正常，不允许添加,msisdn:{}",msisdn);
            throw new MySelfValidateException(ValidationEnum.TERMINAL_USER_STATUS_INVALID);
        }
        //查看客户状态，无效时不允许添加
        CustomerVo customerVo=new CustomerVo();
        customerVo.setCustomerCode(terminalWhiteVo.getCustomerCode());
        customerVo=customerMapper.selectCustomerByCustomerCode(customerVo);
        if(customerVo.getStatus()==0){
            log.error("添加白名单客户状态无效不允许添加,客户名称：{}",customerVo.getName());
            throw new MySelfValidateException(ValidationEnum.CUSTOMER_CUSTOMER_STATUS_INVALID);
        }

        if(ValidateUtil.isEmpty(terminalWhiteVo.getServiceCodes())){
            return 0;
        }

        for(String serviceCode:terminalWhiteVo.getServiceCodes()){

            // 根据客户编码和业务标识查询service_id
            ServiceVo serviceVo = new ServiceVo();
            serviceVo.setCustomerCode(terminalWhiteVo.getCustomerCode());
            serviceVo.setServiceCode(serviceCode);
            ServiceVo serviceVo1 = businessService.selectById(serviceVo);

            if(ValidateUtil.isEmpty(serviceVo1)){
               log.error("添加终端白名单,根据客户编码：{},业务标识：{} 查不到信息",
                       serviceVo.getCustomerCode(),serviceVo.getServiceCode());
               throw new MySelfValidateException(ValidationEnum.SERVICE_SERVICECODE_CUSTERCODE_NOEXISTS);
            }

            callbackUrl=serviceVo1.getCallbackUrl();

            ProductionApiRelationVo productionApiRelationVo = new ProductionApiRelationVo();
            // 根据销售品编码查询对应的api
            productionApiRelationVo.setSaleCode(serviceVo1.getProductId());
            productionApiRelationVo.setProductStatus(1);
            List<ProductionApiRelationVo> list = productionApiRelationService.select(productionApiRelationVo);
            if(ValidateUtil.isEmpty(list)){
                log.error("添加白名单，根据销售品编码：{} 没有查到有效的销售品。",productionApiRelationVo.getSaleCode());
                throw  new MySelfValidateException(ValidationEnum.PRODUCTION_SALECODE_RELATION_NOEXISTS);
            }

            // 服务类型列表(去重)
            List<String> apiCatagoryList = list.stream().map(a->a.getApiCategory()).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            if(ValidateUtil.isEmpty(apiCatagoryList)){
                log.error("添加白名单，根据销售品编码：{}，找不到绑定的api信息",productionApiRelationVo.getSaleCode());
                throw new MySelfValidateException(ValidationEnum.PRODUCT_API_NOEXISTS);
            }
            // 同步插入终端回调关系表
            TerminalCallbackVo terminalCallbackVo = new TerminalCallbackVo();
            terminalCallbackVo.setTerminalId(terminalWhiteVo.getTerminalId());
            terminalCallbackVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            terminalCallbackVo.setUpdateTime(terminalCallbackVo.getCreateTime());
            terminalCallbackVo.setCreaterId(terminalWhiteVo.getCreaterId());
            terminalCallbackVo.setTenantId(terminalWhiteVo.getTenantId());
            terminalCallbackVo.setServiceId(serviceVo1.getId());
            terminalCallbackVo.setStatus(terminalWhiteVo.getStatus());
            terminalCallbackVo.setApiCategoryList(apiCatagoryList);
            terminalCallbackService.insert(terminalCallbackVo);

//            // 把msisdn和服务类型对应的回调url放入redis
//            if(ValidateUtil.isNotEmpty(apiCatagoryList)){
//                RMap<String,String> rMap=redissonUtils.getMap(RedisKeyConst.OM_MSISDNCALLBACKURL_KEY);
//                for(String str:apiCatagoryList){
//                    // 有效状态添加
//                    if(terminalWhiteVo.getStatus().equals(1)){
//                        rMap.put(msisdn+":"+str,callbackUrl);
//                    }// 无效状态移除
//                    else if (terminalWhiteVo.getStatus().equals(0)){
//                        rMap.remove(msisdn+":"+str);
//                    }
//                }
//            }
        }
        // 插入白名单表
        terminalWhiteVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        terminalWhiteVo.setUpdateTime(terminalWhiteVo.getCreateTime());
        int count = terminalWhiteMapper.insert(terminalWhiteVo);
        // 同步redis,业务标识对应的终端列表
        RBucket<String> bucket = redissonUtils.getBucket(RedisKeyConst.GATEWAY_SERVICE_WHILTE_DIR + terminalWhiteVo.getServiceCode());
        ServiceCodeMsisdnsDto serviceCodeMsisdnsDto = new ServiceCodeMsisdnsDto();
        if(bucket.isExists()){
            try{
                serviceCodeMsisdnsDto = JsonUtil.json2Object(bucket.get(),ServiceCodeMsisdnsDto.class);
            }catch(JsonProcessingException e){
                log.error("jsonString转换成Object对象异常：{}",e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

        }
        if(ValidateUtil.isEmpty(serviceCodeMsisdnsDto.getMsisdnList())){
            ArrayList<String> list=new ArrayList<>();
            list.add(msisdn);
            serviceCodeMsisdnsDto.setMsisdnList(list);
        }else{
            serviceCodeMsisdnsDto.getMsisdnList().add(msisdn);
        }
        bucket.set(JsonUtil.objectToJsonSetDateFormat(serviceCodeMsisdnsDto));
        return count;
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertUpload(TerminalWhiteVo terminalWhiteVo) {
        String msisdn = terminalWhiteVo.getMsisdn();
        String callbackUrl = "";
        for(String serviceCode:terminalWhiteVo.getServiceCodes()){
            // 根据客户编码和业务标识查询service_id
            ServiceVo serviceVo = new ServiceVo();
            serviceVo.setCustomerCode(terminalWhiteVo.getCustomerCode());
            serviceVo.setServiceCode(serviceCode);
            ServiceVo serviceVo1 = businessService.selectById(serviceVo);
            if(ValidateUtil.isEmpty(serviceVo1)){
                log.error("添加终端白名单,根据客户编码：{},业务标识：{} 查不到信息",
                        serviceVo.getCustomerCode(),serviceVo.getServiceCode());
                throw new MySelfValidateException(ValidationEnum.SERVICE_SERVICECODE_CUSTERCODE_NOEXISTS);
            }
            callbackUrl=serviceVo1.getCallbackUrl();
            ProductionApiRelationVo productionApiRelationVo = new ProductionApiRelationVo();
            // 根据销售品编码查询对应的api
            productionApiRelationVo.setSaleCode(serviceVo1.getProductId());
            productionApiRelationVo.setProductStatus(1);
            List<ProductionApiRelationVo> list = productionApiRelationService.select(productionApiRelationVo);
            if(ValidateUtil.isEmpty(list)){
                log.error("添加白名单，根据销售品编码：{} 没有查到有效的销售品。",productionApiRelationVo.getSaleCode());
                throw  new MySelfValidateException(ValidationEnum.PRODUCTION_SALECODE_RELATION_NOEXISTS);
            }
            // 服务类型列表(去重)
            List<String> apiCatagoryList = list.stream().map(a->a.getApiCategory()).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
            if(ValidateUtil.isEmpty(apiCatagoryList)){
                log.error("添加白名单，根据销售品编码：{}，找不到绑定的api信息",productionApiRelationVo.getSaleCode());
                throw new MySelfValidateException(ValidationEnum.PRODUCT_API_NOEXISTS);
            }
            // 同步插入终端回调关系表
            TerminalCallbackVo terminalCallbackVo = new TerminalCallbackVo();
            terminalCallbackVo.setTerminalId(terminalWhiteVo.getTerminalId());
            terminalCallbackVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            terminalCallbackVo.setUpdateTime(terminalCallbackVo.getCreateTime());
            terminalCallbackVo.setCreaterId(terminalWhiteVo.getCreaterId());
            terminalCallbackVo.setTenantId(terminalWhiteVo.getTenantId());
            terminalCallbackVo.setServiceId(serviceVo1.getId());
            terminalCallbackVo.setStatus(terminalWhiteVo.getStatus());
            terminalCallbackVo.setApiCategoryList(apiCatagoryList);
            terminalCallbackService.insert(terminalCallbackVo);

//            // 把msisdn和服务类型对应的回调url放入redis
//            if(ValidateUtil.isNotEmpty(apiCatagoryList)){
//                RMap<String,String> rMap=redissonUtils.getMap(RedisKeyConst.OM_MSISDNCALLBACKURL_KEY);
//                for(String str:apiCatagoryList){
//                    // 有效状态添加
//                    if(terminalWhiteVo.getStatus().equals(1)){
//                        rMap.put(msisdn+":"+str,callbackUrl);
//                    }// 无效状态移除
//                    else if (terminalWhiteVo.getStatus().equals(0)){
//                        rMap.remove(msisdn+":"+str);
//                    }
//                }
//            }
        }
        // 插入白名单表
        terminalWhiteVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        terminalWhiteVo.setUpdateTime(terminalWhiteVo.getCreateTime());
        // 同步redis,业务标识对应的终端列表
        RBucket<String> bucket = redissonUtils.getBucket(RedisKeyConst.GATEWAY_SERVICE_WHILTE_DIR + terminalWhiteVo.getServiceCode());
        ServiceCodeMsisdnsDto serviceCodeMsisdnsDto = new ServiceCodeMsisdnsDto();
        if(bucket.isExists()){
            try{
                serviceCodeMsisdnsDto = JsonUtil.json2Object(bucket.get(),ServiceCodeMsisdnsDto.class);
            }catch(JsonProcessingException e){
                log.error("jsonString转换成Object对象异常：{}",e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

        }
        if(ValidateUtil.isEmpty(serviceCodeMsisdnsDto.getMsisdnList())){
            ArrayList<String> list=new ArrayList<>();
            list.add(msisdn);
            serviceCodeMsisdnsDto.setMsisdnList(list);
        }else{
            serviceCodeMsisdnsDto.getMsisdnList().add(msisdn);
        }
        bucket.set(JsonUtil.objectToJsonSetDateFormat(serviceCodeMsisdnsDto));
    }

    /**
     * 更新
     * @param terminalWhiteVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TerminalWhiteVo terminalWhiteVo){

        String msisdn = terminalWhiteVo.getMsisdn();

        // 根据msisdn查询terminalid
        TerminalVo terminalVo =new TerminalVo();
        terminalVo.setMsisdn(terminalWhiteVo.getMsisdn());
        List<TerminalVo> terminalVos=terminalService.selectAll(terminalVo);

        if(ValidateUtil.isNotEmpty(terminalVos)){
            String terminalId = terminalVos.get(0).getId();
            terminalWhiteVo.setTerminalId(terminalId);

//            TerminalCallbackVo terminalCallbackVo = new TerminalCallbackVo();
//            terminalCallbackVo.setTerminalId(terminalId);
//            //先根据terminalId查询回调关系表
//            List<TerminalCallbackVo> terminalCallbackVos = terminalCallbackMapper.select(null,terminalCallbackVo);
//            // 根据terminalId删除回调关系表
//            terminalCallbackMapper.delete(terminalCallbackVo);

//            // 移除reids中 msisdn+服务类型的key
//            RMap<String,String> rMap=redissonUtils.getMap(RedisKeyConst.OM_MSISDNCALLBACKURL_KEY);
//            terminalCallbackVos.stream().forEach(terminalCallbackVo1 -> {
//                rMap.remove(msisdn+":"+terminalCallbackVo1.getApiCategory());
//            });

            String callbackUrl=null;
            // 根据客户编码和业务标识查询service_id
            ServiceVo serviceVo = new ServiceVo();
            serviceVo.setCustomerCode(terminalWhiteVo.getCustomerCode());
            serviceVo.setServiceCode(terminalWhiteVo.getServiceCode());
            ServiceVo serviceVo1 = businessService.selectById(serviceVo);

//            if (ValidateUtil.isEmpty(serviceVo1)) {
//                log.error("添加终端白名单,根据客户编码：{},业务标识：{} 查不到信息",
//                        serviceVo.getCustomerCode(), serviceVo.getServiceCode());
//                throw new MySelfValidateException(ValidationEnum.SERVICE_SERVICECODE_CUSTERCODE_NOEXISTS);
//            }
//
//            callbackUrl = serviceVo1.getCallbackUrl();
//
//            ProductionApiRelationVo productionApiRelationVo1 = new ProductionApiRelationVo();
//            // 根据销售品编码查询对应的api
//            productionApiRelationVo1.setSaleCode(serviceVo1.getProductId());
//            List<ProductionApiRelationVo> list = productionApiRelationService.select(productionApiRelationVo1);
//            if (ValidateUtil.isEmpty(list)) {
//                log.error("添加白名单，根绝销售品编码：{} 没有查到销售品关联的api信息。", productionApiRelationVo1.getSaleCode());
//                throw new MySelfValidateException(ValidationEnum.PRODUCTION_SALECODE_RELATION_NOEXISTS);
//            }
//
//            // 服务类型列表(去重)
//            List<String> apiCatagoryList = list.stream().map(a -> a.getApiCategory()).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
//            // 同步插入终端回调关系表
//            TerminalCallbackVo terminalCallbackVo1 = new TerminalCallbackVo();
//            terminalCallbackVo1.setTerminalId(terminalWhiteVo.getTerminalId());
//            terminalCallbackVo1.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
//            terminalCallbackVo1.setUpdateTime(terminalCallbackVo1.getCreateTime());
//            terminalCallbackVo1.setCreaterId(terminalWhiteVo.getCreaterId());
//            terminalCallbackVo1.setTenantId(terminalWhiteVo.getTenantId());
//            terminalCallbackVo1.setServiceId(serviceVo1.getId());
//            terminalCallbackVo1.setStatus(terminalWhiteVo.getStatus());
//            terminalCallbackVo1.setApiCategoryList(apiCatagoryList);
//            terminalCallbackService.insert(terminalCallbackVo1);

            TerminalCallbackVo terminalCallbackVo1 = new TerminalCallbackVo();
            terminalCallbackVo1.setTerminalId(terminalWhiteVo.getTerminalId());
            terminalCallbackVo1.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            terminalCallbackVo1.setServiceId(serviceVo1.getId());
            terminalCallbackVo1.setStatus(terminalWhiteVo.getStatus());
            terminalCallbackService.update(terminalCallbackVo1);

//            // 把msisdn和服务类型对应的回调url放入redis
//            if (ValidateUtil.isNotEmpty(apiCatagoryList)) {
//                RMap<String, String> rMap1 = redissonUtils.getMap(RedisKeyConst.OM_MSISDNCALLBACKURL_KEY);
//                for (String str : apiCatagoryList) {
//                    // 有效状态添加
//                    if (terminalWhiteVo.getStatus().equals(1)) {
//                        rMap1.put(msisdn + ":" + str, callbackUrl);
//                    }// 无效状态移除
//                    else if (terminalWhiteVo.getStatus().equals(0)) {
//                        rMap1.remove(msisdn + ":" + str);
//                    }
//                }
//            }

        }
//        // 查询老数据
//        TerminalWhiteVo terminalWhiteVo1 = new TerminalWhiteVo();
//        terminalWhiteVo1.setId(terminalWhiteVo.getId());
//        TerminalWhitePo terminalWhitePo = terminalWhiteMapper.selectById("mgt_terminal_white",terminalWhiteVo.getId());

        // 同步redis数据
        RBucket<String> bucket = redissonUtils.getBucket(RedisKeyConst.GATEWAY_SERVICE_WHILTE_DIR + terminalWhiteVo.getServiceCode());
        try{
            ServiceCodeMsisdnsDto serviceCodeMsisdnsDto = JsonUtil.json2Object(bucket.get(),ServiceCodeMsisdnsDto.class);
            List<String> msisdnList= serviceCodeMsisdnsDto.getMsisdnList();
            if(ValidateUtil.isNotEmpty(msisdnList)){
                if(terminalWhiteVo.getStatus()==1){
                    msisdnList.remove(msisdn);
                }else{
                    msisdnList.add(msisdn);
                }
                serviceCodeMsisdnsDto.setMsisdnList(msisdnList);
                bucket.set(JsonUtil.objectToJsonSetDateFormat(serviceCodeMsisdnsDto));
            }
        }catch (JsonProcessingException e){
            log.error("jsonString转Object异常：{}",e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

        // 更新白名单
        terminalWhiteVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        int count =  terminalWhiteMapper.update(terminalWhiteVo);

        return count;
    }



    /**
     * 分页查询
     * @param page
     * @param terminalWhiteVo
     * @return
     */
    @Override
    public List<TerminalWhiteVo> select(Page<?> page, TerminalWhiteVo terminalWhiteVo) {
        if(ValidateUtil.isNotEmpty(terminalWhiteVo.getBindTimeList())){
            terminalWhiteVo.setCreateBeginTime(terminalWhiteVo.getBindTimeList().get(0));
            terminalWhiteVo.setCreateEndTime(terminalWhiteVo.getBindTimeList().get(1));
        }
        return terminalWhiteMapper.select(page,terminalWhiteVo);
    }

    /**
     * 不分页查询
     * @param terminalWhiteVo
     * @return
     */
    @Override
    public List<TerminalWhiteVo> selectAll(TerminalWhiteVo terminalWhiteVo) {
        return terminalWhiteMapper.selectAll(terminalWhiteVo);
    }

    /**
     * 导出记录
     * @param terminalWhiteVo
     * @param response
     */
	@Override
	public void export(TerminalWhiteVo terminalWhiteVo, HttpServletResponse response) {
        //GET请求会将+转化为空格,处理MSISDN
        terminalWhiteVo.setMsisdn(terminalWhiteVo.getMsisdn().replace(' ','+'));
        if(ValidateUtil.isNotEmpty(terminalWhiteVo.getBindTimeList())){
            terminalWhiteVo.setCreateBeginTime(terminalWhiteVo.getBindTimeList().get(0));
            terminalWhiteVo.setCreateEndTime(terminalWhiteVo.getBindTimeList().get(1));
        }
	    List<TerminalWhiteVo> list = selectAll(terminalWhiteVo);
        // 查询字典表
        DictVo dictVo = new DictVo();
        // 终端类型
        dictVo.setType("terminal_type");
        List<DictVo> terminalTypeList = dictService.selectAll(dictVo);
        // 终端用户状态
        dictVo.setType("terminal_user_status");
        List<DictVo> userStatusList = dictService.selectAll(dictVo);
        if(ValidateUtil.isNotEmpty(list)){
            list.stream().forEach(terminalWhiteVo1 -> {
                DictVo dictVo1 = terminalTypeList.stream().filter(a->a.getCode().equals(terminalWhiteVo1.getType())).findFirst().get();
                terminalWhiteVo1.setTypeStr(dictVo1.getDesc());
                dictVo1 = userStatusList.stream().filter(a->a.getCode().equals(terminalWhiteVo1.getUserStatus())).findFirst().get();
                terminalWhiteVo1.setUserStatusStr(dictVo1.getDesc());

                if(terminalWhiteVo1.getStatus()==1){
                    terminalWhiteVo1.setStatusStr("有效");
                }else{
                    terminalWhiteVo1.setStatusStr("无效");
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
        writer.addHeaderAlias("typeStr","终端类型");
        writer.addHeaderAlias("customerName","终端客户名称");
        writer.addHeaderAlias("serviceCode","业务标识");
        writer.addHeaderAlias("statusStr","終端用户状态");
        writer.addHeaderAlias("","审核状态");
        writer.addHeaderAlias("createrName","创建人");
        writer.addHeaderAlias("createTime","创建时间");
        writer.write(list);
        //terminalwhite.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=terminalwhite.xls");
        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            log.error("终端用户白名单导出异常：",e);
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
        writer.setColumnWidth(3,100);
        // 必须设置，否则多首列
        writer.setOnlyAlias(true);
        writer.write(errorList);
        //terminalwhite.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=terminalwhiteerror.xls");
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
     * 导入终端用户白名单
     * @return List<String>
     * @param file
     * @param createrId
     * @return Response
     */
	@Override
    @Transactional(rollbackFor = Exception.class)
	public Response uploadterminalwhite(MultipartFile file, String createrId, HttpServletResponse httpServletResponse, HttpServletRequest request) {
			long startTime = System.currentTimeMillis();
		
		
			String msg="";
	        String filename = file.getOriginalFilename();
	        if(!filename.matches(FILENAMEREGEX)){
	            log.error("批量导入模板文件名错误：{}！",filename);
	            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_TERMINALWHITE_NOTEXISTS);
	        }
	        ExcelReader excelReader = null;
	        try {
	            excelReader = ExcelUtil.getReader(file.getInputStream());
	        } catch (IOException e) {
	            log.error("终端白名单导入异常:",e);
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
	        dictVo.setType("terminal_type");
	        List<DictVo> terminalTypeList = dictService.selectAll(dictVo);
	        List<TerminalWhiteVo> addList=new ArrayList<>();
            List<Map<String,Object>> errorList=new ArrayList<>();
            Admin admin= (Admin) request.getSession().getAttribute("adminInfo");
	        readAll.stream().forEach(map->{
                     StringBuilder errorStr=new StringBuilder();
	                 TerminalWhiteVo terminalWhiteVo = new TerminalWhiteVo();
                     terminalWhiteVo.setCreaterId(String.valueOf(admin.getAdminId()));
                     terminalWhiteVo.setCreaterName(String.valueOf(admin.getRealname()));
                     terminalWhiteVo.setTenantId(String.valueOf(admin.getSiteId()));
                     terminalWhiteVo.setOrgId(String.valueOf(admin.getOrgId()));
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
                             terminalWhiteVo.setMsisdn(msisdn.toString());
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
                         }
                         if(errorStr.length()==0){
                             terminalWhiteVo.setCustomerName(customerName.toString());
                         }
                         if(terminalWhiteVo.getMsisdn()!=null&&terminalWhiteVo.getCustomerName()!=null){
                             //客户与用户绑定关系校验
                             TerminalVo terminalVo1=terminalMapper.selectterminalbymsisdnandcustomername(terminalVo);
                             if(terminalVo1==null){
                                 log.warn("导入异常:客户与用户绑定关系不存在");
                                 errorStr.append("|客户与用户绑定关系不存在");
                             }else {
                                 terminalVo=terminalVo1;
                                 terminalWhiteVo.setTerminalId(terminalVo.getId());
                                 DictVo dictVo1=null;
                                 if(map.get("业务标识")==null){
                                     log.warn("导入异常:业务标识格式校验不通过！");
                                     errorStr.append("|业务标识格式校验不通过");
                                 }else {
                                     terminalWhiteVo.setServiceCode(map.get("业务标识").toString());
                                     //查找所有未关联的业务标识
                                     List<ServiceVo> list=serviceMapper.unRelationWhiteServiceCode(terminalWhiteVo);
                                     List<String> Codes=list.stream().map(s->s.getServiceCode()).collect(Collectors.toList());
                                     if (!Codes.contains(terminalWhiteVo.getServiceCode())){
                                         log.error("业务标识不存在或已被添加！");
                                         errorStr.append("|业务标识不存在或已被添加");
                                     }else {
                                         List<String> serviceCodes=new ArrayList<>();
                                         serviceCodes.add(terminalWhiteVo.getServiceCode());
                                         terminalWhiteVo.setServiceCodes(serviceCodes);
                                         terminalWhiteVo.setStatus(1);
                                         terminalWhiteVo.setCustomerCode(terminalVo.getCustomerCode());
                                     }
                                 }
                             }
                         }
                     }
                if(errorStr.length()==0){
                    try {
                        insertUpload(terminalWhiteVo);
                    }catch (MySelfValidateException e){
                        errorStr.append("|"+e.getValue());
                    }
                    if(errorStr.length()==0){
                        addList.add(terminalWhiteVo);
                    }
                }
                if(errorStr.length()>0){
                    map.put("异常信息",errorStr.substring(1));
                    errorList.add(map);
                }
	        });
        if(ValidateUtil.isNotEmpty(addList)){
            terminalWhiteMapper.insertList(addList);
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
     * 查询有效业务标识
     * @param terminalWhiteVo
     * @return
     */
    @Override
    public List<ServiceVo> selectServiceCode(TerminalWhiteVo terminalWhiteVo) {
        return serviceMapper.unRelationWhiteServiceCode(terminalWhiteVo);
    }



}
