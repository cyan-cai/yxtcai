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
import com.java.yxt.dao.TerminalFactoryMapper;
import com.java.yxt.dao.TerminalMapper;
import com.java.yxt.dao.UploadErrorMapper;
import com.java.yxt.dto.ShortDataChargingStatusNotifyDto;
import com.java.yxt.enums.ServerEnum;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.feign.ProtocolFeignService;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.po.UpLoadErrorPo;
import com.java.yxt.service.DictService;
import com.java.yxt.service.TerminalService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.RandomUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.DictVo;
import com.java.yxt.vo.RelationTerminalVo;
import com.java.yxt.vo.TerminalVo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * TerminalServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description 终端管理
 * @Package com.java.yxt.service.impl
 * @date 2020/9/15
 */
@Service
@Slf4j
@EnableAsync
public class TerminalServiceImpl implements TerminalService {

    @Autowired
    TerminalMapper terminalMapper;
    @Autowired
    TerminalFactoryMapper terminalFactoryMapper;

    @Autowired
    DictService dictService;

    @Autowired
    RedissonClient redissonUtils;

    @Autowired
    ProtocolFeignService protocolFeignService;

    @Autowired
    UploadErrorMapper uploadErrorMapper;
    //校验导入文件名正则
    public static final String FILENAMEREGEX="^terminaluser{1}.*\\.xls{1}$";
    //校验URL地址正则
    public static final String URLREGEX="^((ht|f)tps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:\\/~\\+#]*[\\w\\-\\@?^=%&\\/~\\+#])?$";
    //校验MSISDN正则
    public static final String MSISDNREGEX="^\\+{0,1}[0-9]+\\-{0,1}[0-9]+$";
    //校验IMSI正则
    public static final String IMSIREGEX="^[a-z0-9A-Z]{15}$";
    //异常文件保存目录
    public static final String DIRNAME="src/main/resources/static/errorExcel";
    /**
     * 插入
     * @param terminalVo
     * @return
     */
    @Override
    public int insert(TerminalVo terminalVo) {
        terminalVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        //判断用户名称是否存在
        if(terminalMapper.selectterminalbyname(terminalVo)!=null){
            log.warn("用户名称已存在：{}！",terminalVo.getMsisdn());
            throw  new MySelfValidateException(ValidationEnum.TERMINAL_USERNAME_MSISDN_EXISTS);
        }
        //判断msisdn是否存在
        if(terminalMapper.selectterminalbymsisdn(terminalVo)!=null){
            log.warn("用户msisdn已存在：{}！",terminalVo.getMsisdn());
            throw  new MySelfValidateException(ValidationEnum.TERMINAL_USERNAME_MSISDN_EXISTS);
        }
        // 调用协议网关计费标识通知
        ShortDataChargingStatusNotifyDto shortDataChargingStatusNotifyDto = new ShortDataChargingStatusNotifyDto();
        shortDataChargingStatusNotifyDto.setServerId(ServerEnum.SP_SERVER.getServerId().shortValue());
        String dateTime = DateTimeUtil.getBasicDateTime();
        String randomNumber = RandomUtil.number(5);
        String requestId = dateTime + randomNumber;
        shortDataChargingStatusNotifyDto.setRequestId(requestId);
        shortDataChargingStatusNotifyDto.setMsisdn(terminalVo.getMsisdn());
        shortDataChargingStatusNotifyDto.setChargeIdentity(terminalVo.getCharge().byteValue());
        protocolFeignService.chargingNotify(shortDataChargingStatusNotifyDto);
        return terminalMapper.insert(terminalVo);
    }

    /**
     * 导入插入
     * @param terminalVo
     * @return
     */

    @Override
    public int insertupload(TerminalVo terminalVo) {
        return terminalMapper.insertupload(terminalVo);
    }



    /**
     * 更新
     * @param terminalVo
     * @return
     */
    @Override
    @SetCreaterName
    @SOLog(type = OperationType.put,tableName = "mgt_terminal",
            remark = "更新终端用户",mapperName = "terminalMapper",paramKey = "id")
    public int update(TerminalVo terminalVo) {
        // 调用协议网关计费标识通知
        ShortDataChargingStatusNotifyDto shortDataChargingStatusNotifyDto = new ShortDataChargingStatusNotifyDto();
        shortDataChargingStatusNotifyDto.setServerId(ServerEnum.SP_SERVER.getServerId().shortValue());
        String dateTime = DateTimeUtil.getBasicDateTime();
        String randomNumber = RandomUtil.number(5);
        String requestId = dateTime + randomNumber;
        shortDataChargingStatusNotifyDto.setRequestId(requestId);
        shortDataChargingStatusNotifyDto.setMsisdn(terminalVo.getMsisdn());
        shortDataChargingStatusNotifyDto.setChargeIdentity(terminalVo.getCharge().byteValue());
        protocolFeignService.chargingNotify(shortDataChargingStatusNotifyDto);
        return terminalMapper.update(terminalVo);
    }
    /**
     * 根据msisdn更新
     * @param terminalVo
     * @return
     */
    @Override
    @SetCreaterName
    @SOLog(type = OperationType.put,tableName = "mgt_terminal",
            remark = "更新终端用户状态",mapperName = "terminalMapper",paramKey = "id")
    public int updateBymsisdn(TerminalVo terminalVo) {
        return terminalMapper.updateBymsisdn(terminalVo);
    }

    /**
     * 终端关联客户
     * @param relationTerminalVo
     * @return
     */
    @Override
    public int relationCustomer(RelationTerminalVo relationTerminalVo) {
        if(ValidateUtil.isNotEmpty(relationTerminalVo.getUnRelationTerminalVos())) {
            // 先把此客户关联的终端绑定关系解除
            terminalMapper.updateCustomerIdNull(relationTerminalVo.getUnRelationTerminalVos());
        }
            //绑定终端,如果一条都没有则id为空
        if(ValidateUtil.isNotEmpty(relationTerminalVo.getRelationTerminalVos())){
            String customerId=relationTerminalVo.getCustomerId();
            terminalMapper.relationCustomer(relationTerminalVo.getRelationTerminalVos(),customerId);
            }
        return 0;
    }

    /**
     * 关联终端
     * @return
     */
    @Override
    public List<TerminalVo> unRelationCustomer(Page<TerminalVo> page,TerminalVo terminalVo) {
        return terminalMapper.unRelationCustomer(page,terminalVo);
    }

    /**
     * 分页查询
     * @param terminalVo
     * @return
     */
    @Override
    public List<TerminalVo> select(Page page, TerminalVo terminalVo) {
        return terminalMapper.select(page,terminalVo);
    }

    /**
     * 查询
     * @param terminalVo
     * @return
     */
    @Override
    public List<TerminalVo> selectAll(TerminalVo terminalVo) {
        return terminalMapper.selectAll(terminalVo);
    }

    /**
     * 根据终端号码查询客户和终端信息
     * @param terminalVo
     * @return
     */
    @Override
    public List<TerminalVo> selectcustomerbymsisdn(TerminalVo terminalVo) {
        return terminalMapper.selectcustomerbymsisdn(terminalVo);
    }

    /**
     * 根据终端号码查询客户和终端信息(去重)
     * @param terminalVo
     * @return
     */
    @Override
    public List<TerminalVo> selectdistinctcustomerbymsisdn(TerminalVo terminalVo) {
        return terminalMapper.selectdistinctcustomerbymsisdn(terminalVo);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response uploadTerminalUser(MultipartFile file, String createrId, HttpServletResponse httpServletResponse, HttpServletRequest request) {
        String msg="";
        String filename = file.getOriginalFilename();
        if(!filename.matches(FILENAMEREGEX)){
            log.error("批量导入模板文件名错误：{}！",filename);
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_TERMINAL_NOTEXISTS);
        }
        ExcelReader excelReader = null;
        try {
            excelReader = ExcelUtil.getReader(file.getInputStream());
        } catch (IOException e) {
            log.error("终端用户导入异常:",e);
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
        // 用户状态
        dictVo.setType("terminal_user_status");
        List<DictVo> userStatusList = dictService.selectAll(dictVo);
        // 数据来源
        dictVo.setType("terminal_source");
        List<DictVo> sourceList = dictService.selectAll(dictVo);
        // 行业
        dictVo.setType("terminal_industry");
        List<DictVo> industryList = dictService.selectAll(dictVo);
        List<TerminalVo> list=new ArrayList<>();
        List<Map<String,Object>> errorList=new ArrayList<>();
        Admin admin= (Admin) request.getSession().getAttribute("adminInfo");
        readAll.stream().forEach(map->{
            StringBuilder errorStr=new StringBuilder();
            TerminalVo terminalVo = new TerminalVo();
            terminalVo.setCreaterId(String.valueOf(admin.getAdminId()));
            terminalVo.setCreaterName(String.valueOf(admin.getRealname()));
            terminalVo.setTenantId(String.valueOf(admin.getSiteId()));
            terminalVo.setOrgId(String.valueOf(admin.getOrgId()));
            //用户名称校验
            Object userName=map.get("用户名称");
            if(userName==null||userName.toString()==""||userName.toString().length()>100){
                log.warn("导入异常:用户名称格式校验不通过：{}！",userName);
                errorStr.append("|用户名称格式校验不通过");
            }else {
                terminalVo.setUserName(userName.toString());
            }
            //URL校验
            Object url=map.get("URL地址");
           if(url==null||!url.toString().matches(URLREGEX)||url.toString().length()>100){
               log.warn("导入异常:url格式校验不通过：{}！",url);
               errorStr.append("|url格式校验不通过");
           }else {
               terminalVo.setUrl(url.toString());
           }
            //MSISDN校验
            Object msisdn=map.get("MSISDN");
            if(msisdn==null||!msisdn.toString().matches(MSISDNREGEX)||msisdn.toString().length()>21){
                log.warn("导入异常:MSISDN格式校验不通过：{}！",msisdn);
                errorStr.append("|MSISDN格式校验不通过");
            }else {
                terminalVo.setMsisdn(msisdn.toString());
            }
            //IMSI校验
            Object imsi=map.get("IMSI");
            if(imsi==null||!imsi.toString().matches(IMSIREGEX)){
                log.warn("导入异常:IMSI格式校验不通过：{}！",imsi);
                errorStr.append("|IMSI格式校验不通过");
            }else{
                terminalVo.setImsi(imsi.toString());
            }
            DictVo dictVo1=null;
            //终端类型校验
            try {
                String type = map.get("终端类型").toString();
                dictVo1 = terminalTypeList.stream().filter(a->a.getDesc().equals(type)).findFirst().get();
                terminalVo.setType(dictVo1.getCode());
            }catch (Exception e){
                log.warn("导入异常:终端类型格式校验不通过！");
                errorStr.append("|终端类型格式校验不通过");
            }
            //终端厂商校验
            Object terminalFactory=map.get("终端厂商");
            if(terminalFactory==null||terminalFactory.toString()==""||terminalFactory.toString().length()>100){
                log.warn("导入异常:终端厂商格式校验不通过！");
                errorStr.append("|终端厂商格式校验不通过");
            }else{
//                TerminalFactoryVo terminalFactoryVo=new TerminalFactoryVo();
//                terminalFactoryVo.setFactoryName(terminalFactory.toString());
//                if(terminalFactoryMapper.selectByFactoryName(terminalFactoryVo)==null){
//                    log.warn("导入异常:终端厂商不存在：{}！");
//                    errorStr.append("|终端厂商不存在");
//                }else {
                    terminalVo.setTerminalFactory(terminalFactory.toString());
//                }
            }
            //用户状态校验
            try {
                String userstatus = map.get("用户状态").toString();
                dictVo1 = userStatusList.stream().filter(a->a.getDesc().equals(userstatus)).findFirst().get();
                terminalVo.setUserStatus(dictVo1.getCode());
            }catch (Exception e){
                log.warn("导入异常:用户状态格式校验不通过");
                errorStr.append("|用户状态格式校验不通过");
            }
            //数据来源校验
            try {
                String source = map.get("数据来源").toString();
                dictVo1 = sourceList.stream().filter(a->a.getDesc().equals(source)).findFirst().get();
                terminalVo.setSource(dictVo1.getCode());
            }catch (Exception e){
                log.warn("导入异常:数据来源格式校验不通过");
                errorStr.append("|数据来源格式校验不通过");
            }
            //计费类型字段校验
           Object chargeStr = map.get("是否计费");
            if(chargeStr==null||!chargeStr.toString().equals("是")&&!chargeStr.toString().equals("否")){
                log.warn("导入异常:计费类型格式校验不通过：{}！",chargeStr);
                errorStr.append("|计费类型格式校验不通过");
            }else {
                Integer charge = "是".equals(chargeStr.toString()) ? 1 : 0;
                terminalVo.setCharge(charge);
            }
            //行业类型字段校验
            try {
                String industr = (String) map.get("行业类型");
                dictVo1 = industryList.stream().filter(a->a.getDesc().equals(industr)).findFirst().get();
                terminalVo.setIndustry(dictVo1.getCode());
            }catch (Exception e){
                log.warn("导入异常:行业类型格式校验不通过",e);
                errorStr.append("|行业类型格式校验不通过");
            }
            terminalVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            terminalVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
            //判断新增还是修改
            terminalVo.setStatus(1);
            TerminalVo terminalVoByNameAndMsisdn=terminalMapper.selectterminalbynameandmsisdn(terminalVo);
            if(terminalVoByNameAndMsisdn!=null){
                if(errorStr.length()==0){
                    //更新
                    list.add(terminalVo);
                }
            }else {
                //新增
                if(errorStr.length()==0){
                    try{
                        //判断用户名称是否存在
                        if(terminalMapper.selectterminalbyname(terminalVo)!=null){
                            log.warn("用户名称已存在：{}！",terminalVo.getMsisdn());
                            throw  new MySelfValidateException(ValidationEnum.TERMINAL_USERNAME_MSISDN_EXISTS);
                        }
                        //判断msisdn是否存在
                        if(terminalMapper.selectterminalbymsisdn(terminalVo)!=null){
                            log.warn("用户msisdn已存在：{}！",terminalVo.getMsisdn());
                            throw  new MySelfValidateException(ValidationEnum.TERMINAL_USERNAME_MSISDN_EXISTS);
                        }
                    }catch (MySelfValidateException e){
                        errorStr.append("|"+e.getValue());
                    }
                }
                if(errorStr.length()==0){
                    list.add(terminalVo);
                }
            }
            if(errorStr.length()>0){
                map.put("异常信息",errorStr.substring(1));
                errorList.add(map);
            }
        });
        if(ValidateUtil.isNotEmpty(list)){
            terminalMapper.insertList(list);
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
    public void export(TerminalVo terminalVo, HttpServletResponse response) {
        //GET请求会将+转化为空格,处理MSISDN
        terminalVo.setMsisdn(terminalVo.getMsisdn().replace(' ','+'));
        List<TerminalVo> list = selectAll(terminalVo);
        // 查询字典表
        DictVo dictVo = new DictVo();
        // 终端类型
        dictVo.setType("terminal_type");
        List<DictVo> terminalTypeList = dictService.selectAll(dictVo);
        // 用户状态
        dictVo.setType("terminal_user_status");
        List<DictVo> userStatusList = dictService.selectAll(dictVo);
        // 数据来源
        dictVo.setType("terminal_source");
        List<DictVo> sourceList = dictService.selectAll(dictVo);
        // 行业
        dictVo.setType("terminal_industry");
        List<DictVo> industryList = dictService.selectAll(dictVo);

        if(ValidateUtil.isNotEmpty(list)){
            String str1="1";
            String str2="2";
            
            list.stream().forEach(terminalVo1 -> {
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	DictVo dictVo1 = terminalTypeList.stream().filter(a->a.getCode().equals(terminalVo1.getType())).findFirst().get();
                terminalVo1.setTypeStr(dictVo1.getDesc());

                dictVo1 = userStatusList.stream().filter(a->a.getCode().equals(terminalVo1.getUserStatus())).findFirst().get();
                terminalVo1.setUserStatusStr(dictVo1.getDesc());

                dictVo1 = sourceList.stream().filter(a->a.getCode().equals(terminalVo1.getSource())).findFirst().get();
                terminalVo1.setSourceStr(dictVo1.getDesc());

                dictVo1 = industryList.stream().filter(a->a.getCode().equals(terminalVo1.getIndustry())).findFirst().get();
                terminalVo1.setIndustryStr(dictVo1.getDesc());

                Integer charge=terminalVo1.getCharge();
                String chargeStr= charge==1?"是":"否";
                terminalVo1.setChargeStr(chargeStr);
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
        writer.addHeaderAlias("userName","用户名称");
        writer.addHeaderAlias("url","URL地址");
        writer.addHeaderAlias("msisdn","MSISDN");
        writer.addHeaderAlias("imsi","IMSI");
        writer.addHeaderAlias("terminalFactory","终端厂商");
        writer.addHeaderAlias("typeStr","终端类型");
        writer.addHeaderAlias("userStatusStr","用户状态");
        writer.addHeaderAlias("sourceStr","数据来源");
        writer.addHeaderAlias("chargeStr","是否计费");
        writer.addHeaderAlias("industryStr","行业类型");
        writer.write(list);

        //customer.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=terminaluser.xls");
        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            log.error("用户终端导出异常：",e);
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
        writer.setColumnWidth(10,100);
        // 必须设置，否则多首列
        writer.setOnlyAlias(true);
        writer.write(errorList);
        //customer.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=terminalusererror.xls");
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
