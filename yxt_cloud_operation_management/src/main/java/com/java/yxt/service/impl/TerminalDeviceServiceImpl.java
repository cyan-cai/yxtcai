package com.java.yxt.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.base.Response;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.dao.*;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.po.DeviceInfoPo;
import com.java.yxt.po.UpLoadErrorPo;
import com.java.yxt.service.DictService;
import com.java.yxt.service.TerminalDeviceService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.DictVo;
import com.java.yxt.vo.TerminalDeviceVo;
import com.java.yxt.vo.TerminalFactoryVo;
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
import java.util.*;


/**
 * TerminalDeviceServiceImpl
 *
 * @author 蔡家明
 * @version V1.0
 * @description 终端设备管理
 * @Package com.java.yxt.service.impl
 * @date 2020/12/8
 */
@Service
@Slf4j
public class TerminalDeviceServiceImpl implements TerminalDeviceService {

    @Autowired
    TerminalDeviceMapper terminalDeviceMapper;
    @Autowired
    TerminalFactoryMapper terminalFactoryMapper;
    @Autowired
    TerminalAuditMapper terminalAuditMapper;
    @Autowired
    DeviceInfoMapper deviceInfoMapper;
    @Autowired
    UploadErrorMapper uploadErrorMapper;
    @Autowired
    DictService dictService;
    @Autowired
    RedissonClient redissonUtils;
    //校验导入文件名正则
    public static final String FILENAMEREGEX="^terminaldevice{1}.*\\.xls{1}$";
    //IMEI正则
    public static final String IMEIREGEX="^\\d{17}|\\d{15}$";
    //MEID正则
    public static final String MEIDREGEX="^\\w{15}$";
    /**
     * 分页查询
     * @param terminalDeviceVo
     * @param page
     * @return
     */
    @Override
    public List<TerminalDeviceVo> select(Page page, TerminalDeviceVo terminalDeviceVo,HttpServletRequest request) {
        String factory=getFactory(request);
        if(factory!=null){
            terminalDeviceVo.setTerminalFactory(factory);
        }
        //设置查询时间段
        if(ValidateUtil.isNotEmpty(terminalDeviceVo.getCreateTimeList())){
            terminalDeviceVo.setCreateBeginTime(terminalDeviceVo.getCreateTimeList().get(0));
            terminalDeviceVo.setCreateEndTime(terminalDeviceVo.getCreateTimeList().get(1));
        }
        return terminalDeviceMapper.select(page, terminalDeviceVo);
    }


    /**
     * 不分页查询
     * @param terminalDeviceVo
     * @return
     */
    @Override
    public List<TerminalDeviceVo> selectAll(TerminalDeviceVo terminalDeviceVo) {
        //设置查询时间段
        if(ValidateUtil.isNotEmpty(terminalDeviceVo.getCreateTimeList())){
            terminalDeviceVo.setCreateBeginTime(terminalDeviceVo.getCreateTimeList().get(0));
            terminalDeviceVo.setCreateEndTime(terminalDeviceVo.getCreateTimeList().get(1));
        }
        return terminalDeviceMapper.selectAll(terminalDeviceVo);
    }

    /**
     * 插入
     * @param terminalDeviceVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(TerminalDeviceVo terminalDeviceVo,HttpServletRequest request) {
        String factory=getFactory(request);
        if(factory!=null){
            terminalDeviceVo.setTerminalFactory(factory);
        }
        terminalDeviceVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        terminalDeviceVo.setAuditStatus(1);
        //查看终端设备IMEI或MEID是否存在
        if(terminalDeviceMapper.selectByIMEI(terminalDeviceVo)!=null){
            log.warn("终端设备IMEI已存在：{}！",terminalDeviceVo.getImei());
            throw  new MySelfValidateException(ValidationEnum.TERMINAL_DEVICE_IMEI_EXISTS);
        }else if (terminalDeviceMapper.selectByMEID(terminalDeviceVo)!=null){
            log.warn("终端设备MEID已存在：{}！",terminalDeviceVo.getMeid());
            throw  new MySelfValidateException(ValidationEnum.TERMINAL_DEVICE_MEID_EXISTS);
        }
        terminalDeviceVo.setStatus(1);
        return terminalDeviceMapper.insert(terminalDeviceVo);
    }

    /**
     * 更新
     * @param terminalDeviceVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TerminalDeviceVo terminalDeviceVo) {
        terminalDeviceVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return terminalDeviceMapper.update(terminalDeviceVo);
    }

    /**
     * 批量提交审核
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBatch(List<TerminalDeviceVo> list) {
        //排除不允许提交的设备状态
        Iterator<TerminalDeviceVo> itr = list.iterator();
        while (itr.hasNext()) {
            TerminalDeviceVo terminalDeviceVo = itr.next();
            if(terminalDeviceVo.getAuditStatus()!=1 && terminalDeviceVo.getAuditStatus()!=4) {
                itr.remove();
            }
        }
        return terminalDeviceMapper.updateBatch(list);
    }

    /**
     * 删除
     * @param terminalDeviceVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(TerminalDeviceVo terminalDeviceVo) {
        return terminalDeviceMapper.delete(terminalDeviceVo);
    }

    /**
     * 导入
     * @param file
     * @param createrId
     * @return String
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response uploadTerminalDevice(MultipartFile file, String createrId,HttpServletResponse response,HttpServletRequest request) {
        String msg="";
        String filename = file.getOriginalFilename();
        if(!filename.matches(FILENAMEREGEX)){
            log.error("批量导入模板文件名错误：{}！",filename);
            throw new MySelfValidateException(ValidationEnum.UPLOADFILE_TERMINALDEVICE_NOTEXISTS);
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
        List<Map<String,Object>> errorList=new ArrayList<>();
        List<TerminalDeviceVo> list=new ArrayList<>();
        Admin admin= (Admin) request.getSession().getAttribute("adminInfo");
        String factory=getFactory(request);
        readAll.stream().forEach(map->{
            StringBuilder errorStr=new StringBuilder();
            TerminalDeviceVo terminalDeviceVo=new TerminalDeviceVo();
            terminalDeviceVo.setCreaterId(String.valueOf(admin.getAdminId()));
            terminalDeviceVo.setCreaterName(String.valueOf(admin.getRealname()));
            terminalDeviceVo.setTenantId(String.valueOf(admin.getSiteId()));
            terminalDeviceVo.setOrgId(String.valueOf(admin.getOrgId()));
            //IMEI校验
            Object imei=map.get("IMEI");
            if(imei==null||!imei.toString().matches(IMEIREGEX)){
                log.warn("导入异常:IMEI格式校验不通过：{}！",imei);
                errorStr.append("|IMEI格式校验不通过");
            }else {
                terminalDeviceVo.setImei(imei.toString());
            }
            DictVo dictVo1=null;
            //终端类型校验
            try {
                String type = map.get("终端类型").toString();
                dictVo1 = terminalTypeList.stream().filter(a->a.getDesc().equals(type)).findFirst().get();
                terminalDeviceVo.setType(dictVo1.getCode());
            }catch (Exception e){
                log.warn("导入异常:终端类型格式校验不通过：{}！");
                errorStr.append("|终端类型格式校验不通过");
            }
            //终端厂商校验
            Object terminalFactory=map.get("终端厂商");
            if(terminalFactory==null||terminalFactory.toString()==""||terminalFactory.toString().length()>40){
                log.warn("导入异常:终端厂商格式校验不通过：{}！");
                errorStr.append("|终端厂商格式校验不通过");
            }else{
                TerminalFactoryVo terminalFactoryVo=new TerminalFactoryVo();
                terminalFactoryVo.setFactoryName(terminalFactory.toString());
                if(terminalFactoryMapper.selectByFactoryName(terminalFactoryVo)==null){
                    log.warn("导入异常:终端厂商不存在：{}！");
                    errorStr.append("|终端厂商不存在");
                }else {
                    terminalDeviceVo.setTerminalFactory(terminalFactory.toString());
                }
            }
            if(factory!=null&&!factory.equals(terminalDeviceVo.getTerminalFactory())){
                log.warn("导入异常:终端厂商与当前登录厂商不符：{}！");
                errorStr.append("|终端厂商与当前登录厂商不符");
            }
            //终端型号校验
            Object model = map.get("终端型号");
            if(model==null||model.toString().length()>50){
                log.warn("导入异常:终端型号格式校验不通过：{}！",imei);
                errorStr.append("|终端型号格式校验不通过");
            }else {
                terminalDeviceVo.setModel(model.toString());
            }
            //MEID
            Object meid=map.get("MEID");
            if(meid==null||!meid.toString().matches(MEIDREGEX)){
                log.warn("导入异常:MEID格式校验不通过：{}！",meid);
                errorStr.append("|MEID格式校验不通过");
            }else {
                terminalDeviceVo.setMeid(meid.toString());
            }
            //判断是否导入更新，状态为待审核及审核通过时不允许更新
            TerminalDeviceVo terminalDeviceByIMEIAndMEID=terminalDeviceMapper.selectByIMEIAndMEID(terminalDeviceVo);
                if(terminalDeviceByIMEIAndMEID!=null){
                    //更新
                    if(terminalDeviceByIMEIAndMEID.getAuditStatus()==2||terminalDeviceByIMEIAndMEID.getAuditStatus()==3){
                        log.warn("导入异常:终端设备状态为待审核或审核通过，无法更新：{}！",meid);
                        errorStr.append("|终端设备状态为待审核或审核通过，无法更新");
                    }else if(errorStr.length()==0){
                        list.add(terminalDeviceVo);
                        return;
                    }
                }
            if(errorStr.length()==0){
                try {
                    terminalDeviceVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
                    terminalDeviceVo.setAuditStatus(1);
                    //查看终端设备IMEI或MEID是否存在
                    if(terminalDeviceMapper.selectByIMEI(terminalDeviceVo)!=null){
                        log.warn("终端设备IMEI已存在：{}！",terminalDeviceVo.getImei());
                        throw  new MySelfValidateException(ValidationEnum.TERMINAL_DEVICE_IMEI_EXISTS);
                    }else if (terminalDeviceMapper.selectByMEID(terminalDeviceVo)!=null){
                        log.warn("终端设备MEID已存在：{}！",terminalDeviceVo.getMeid());
                        throw  new MySelfValidateException(ValidationEnum.TERMINAL_DEVICE_MEID_EXISTS);
                    }
                }catch (MySelfValidateException e){
                    errorStr.append("|"+e.getValue());
                }
            }
            if(errorStr.length()==0){
                terminalDeviceVo.setStatus(1);
                list.add(terminalDeviceVo);
            }else{
                map.put("异常信息",errorStr.substring(1));
                errorList.add(map);
            }
        });
        if(ValidateUtil.isNotEmpty(list)){
            terminalDeviceMapper.insertBatch(list);
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
     * 导出
     * @param terminalDeviceVo
     * @param response
     * @return
     */
    @Override
    public void export(TerminalDeviceVo terminalDeviceVo, HttpServletResponse response) {
        RBucket<String> bucket = redissonUtils.getBucket(terminalDeviceVo.getToken());
        Admin admin = JSONObject.parseObject(bucket.get(), Admin.class);
        if(terminalFactoryMapper.selectById(String.valueOf(admin.getOrgId()))!=null){
           terminalDeviceVo.setTerminalFactory(admin.getOrgName());
        }
        List<TerminalDeviceVo> list=selectAll(terminalDeviceVo);
        // 查询字典表
        DictVo dictVo = new DictVo();
        // 终端类型
        dictVo.setType("terminal_type");
        List<DictVo> terminalTypeList = dictService.selectAll(dictVo);
        //设备审核状态
        dictVo.setType("terminal_audit");
        List<DictVo> terminalAuditList = dictService.selectAll(dictVo);
        if(ValidateUtil.isNotEmpty(list)){
            list.stream().forEach(terminalDeviceVo1 -> {
                //设置终端类型字符串
                DictVo dictVo1 = terminalTypeList.stream().filter(a->a.getCode().equals(terminalDeviceVo1.getType())).findFirst().get();
                terminalDeviceVo1.setTypeStr(dictVo1.getDesc());
                //设置终端设备审核状态字符串
                dictVo1 = terminalAuditList.stream().filter(a->a.getCode().equals(terminalDeviceVo1.getAuditStatus())).findFirst().get();
                terminalDeviceVo1.setAuditStatusStr(dictVo1.getDesc());
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
        writer.addHeaderAlias("imei","IMEI");
        writer.addHeaderAlias("typeStr","终端类型");
        writer.addHeaderAlias("terminalFactory","终端厂商");
        writer.addHeaderAlias("model","终端型号");
        writer.addHeaderAlias("meid","MEID");
        writer.addHeaderAlias("auditStatusStr","审核状态");
        writer.write(list);
        //customer.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=terminaldevice.xls");
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
        writer.setColumnWidth(5,100);
        // 必须设置，否则多首列
        writer.setOnlyAlias(true);
        writer.write(errorList);
        //customer.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition","attachment;filename=terminaldeviceerror.xls");
        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out= null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            log.error("用户终端导出异常：",e);
            throw new MySelfValidateException(ValidationEnum.EXPORTFILE_OUTPUTSTREAM_ERROR);
        }finally {
            // 关闭writer，释放内存
            writer.flush(out);
            writer.close();
        }
    }


    /**
     * 同步设备信息至核心网
     */
    @Override
    public String updateInfo() {
        List<DeviceInfoPo> list=deviceInfoMapper.selectAll();
        StringBuilder info=new StringBuilder();
        for (DeviceInfoPo deviceInfoPo:list) {
            info.append(deviceInfoPo.toString()+"\n");
        }
        return info.toString();
    }

    /**
     * 清空设备信息管理表
     */
    @Override
    public int clearInfo() {
        return deviceInfoMapper.delete();
    }

    /**
     * 检查用户权限判断是否是终端厂商
     * @param  request
     * @return Response
     */
    @Override
    public Response checkUser(HttpServletRequest request) {
        Admin admin= (Admin) request.getSession().getAttribute("adminInfo");
        if(terminalFactoryMapper.selectById(String.valueOf(admin.getOrgId()))!=null){
            return new Response(1,admin.getOrgName());
        }
        return new Response(0,"");
    }

    private String getFactory(HttpServletRequest request){
        Admin admin= (Admin) request.getSession().getAttribute("adminInfo");
        if(terminalFactoryMapper.selectById(String.valueOf(admin.getOrgId()))!=null){
            return admin.getOrgName();
        }
        return null;
    }
}
