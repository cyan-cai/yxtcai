package com.java.yxt.logger.feign;

import com.java.yxt.logger.feign.entity.StaffOperationLog;
import com.java.yxt.logger.feign.hystrix.StaffOpLogRedisFeignHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "oamsvc",fallback = StaffOpLogRedisFeignHystrix.class)
public interface StaffOpLogRedisFeign {
    String API ="/service/112/solRApi";

    /**
     * 插入api接口调用日志信息
     * @param staffOperationLog
     * @return
     */
    @PostMapping(API)
    public void postLog(StaffOperationLog staffOperationLog);

}
