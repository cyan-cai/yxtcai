package com.java.yxt.logger.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.java.yxt.feign.ResultObject;
import com.java.yxt.logger.feign.entity.SysAlarmLog;
import com.java.yxt.logger.feign.hystrix.SysAlarmLogFeignHystrix;

@FeignClient(value = "oamsvc",fallback = SysAlarmLogFeignHystrix.class)
public interface SysAlarmLogFeign {
    String API = "/service/112/salApi";

    /**
     * 插入api接口调用日志信息
     *
     * @param sysAlarmLog
     * @return
     */
    @PostMapping(API)
    public ResultObject<Integer> postLog(SysAlarmLog sysAlarmLog);

}