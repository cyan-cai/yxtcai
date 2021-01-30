package com.java.yxt.config;

import com.java.yxt.service.TerminalDeviceService;
import com.java.yxt.vo.DeviceInfoVo;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
public class TimeTask {
        public static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        public static ScheduledFuture future=null;
        //每秒
        public static final int SECOND=15;
        //每天
        public static final int DAY=60*60*24;
        //每周
        public static final int WEEK=60*60*24*7;
        //每月
        public static final int MONTH=60*60*24*30;

        public static void task(DeviceInfoVo deviceInfoVo, TerminalDeviceService terminalDeviceService) {
            String dirName=deviceInfoVo.getDirName();
            String frequency=deviceInfoVo.getFrequency();
            if(future!=null){
                future.cancel(true);
            }
            Integer initalDelay=null;
            Runnable runnable = new Runnable() {
                public void run() {
                    String info=terminalDeviceService.updateInfo();
                    InputStream in=FtpUtil.getStringStream(info);
                    String fileName = new SimpleDateFormat("yyyyMMddHHmmss'.csv'").format(System.currentTimeMillis());
                    FtpUtil.uploadFile("192.168.20.32",21,"yhd1","123456","/home/yhd1/","/ftp_data",fileName,in);
                }
            };
            if("实时".equals(frequency)){
                initalDelay=SECOND;
            }
            if("每天".equals(frequency)){
                initalDelay=DAY;
            }
            if("每周".equals(frequency)){
                initalDelay=WEEK;
            }
            if("每月".equals(frequency)){
                initalDelay=MONTH;
            }
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
             future=service.scheduleAtFixedRate(runnable, 0, initalDelay, TimeUnit.SECONDS);
        }

}
