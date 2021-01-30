package com.java.yxt.logger.feign.hystrix;

import com.java.yxt.logger.feign.StaffOpLogRedisFeign;
import com.java.yxt.logger.feign.entity.StaffOperationLog;
import org.springframework.stereotype.Component;

@Component
public class StaffOpLogRedisFeignHystrix implements StaffOpLogRedisFeign {
    @Override
    public void postLog(StaffOperationLog staffOperationLog) {}
}
