package com.java.yxt.logger.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.java.yxt.feign.ResultObject;
import com.java.yxt.logger.feign.entity.StaffOperationLog;
import com.java.yxt.logger.feign.hystrix.StaffOpLogFeignHystrix;

@FeignClient(value = "oamsvc",fallback = StaffOpLogFeignHystrix.class)
public interface StaffOpLogFeign {
    String API = "/service/112/solApi";

    /**
     * 插入api接口调用日志信息
     *
     * @param staffOperationLog
     * @return
     */
    @PostMapping(API)
    public ResultObject<Integer> postLog(StaffOperationLog staffOperationLog);
}