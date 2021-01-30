package com.java.yxt.config;

import com.java.yxt.dao.UploadErrorMapper;
import com.java.yxt.service.TerminalDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.SimpleDateFormat;


@Slf4j
@Component
public class BootSchedule {

    @Autowired
    TerminalDeviceService terminalDeviceService;
    @Autowired
    UploadErrorMapper uploadErrorMapper;
    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.port}")
    private Integer port;
    @Value("${ftp.username}")
    private String username;
    @Value("${ftp.password}")
    private String password;
    @Value("${ftp.basePath}")
    private String basePath;
    @Value("${ftp.filePath}")
    private String filePath;
    // 每天凌晨1点执行一次
    @Scheduled(cron="0 0 1 * * ?")
    public void schedule01(){
        uploadErrorMapper.delete();
    }

    // 同步设备信息至核心网
    //@Scheduled(initialDelay=1000, fixedDelay=10000)
    public void schedule02(){
        String info=terminalDeviceService.updateInfo();
        Boolean flag=false;
        if(info!=null&&info.length()!=0){
            InputStream in=FtpUtil.getStringStream(info);
            String fileName = "EQUIPMENT_INFO_"+new SimpleDateFormat("yyyyMMddHHmmss'.new'").format(System.currentTimeMillis());
            flag=FtpUtil.uploadFile(host,port,username,password,basePath,filePath,fileName,in);
        }
        //上传完成后清空设备信息表
        if(flag){
            terminalDeviceService.clearInfo();
        }
    }
}