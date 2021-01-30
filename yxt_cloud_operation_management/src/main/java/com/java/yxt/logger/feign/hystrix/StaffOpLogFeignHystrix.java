package com.java.yxt.logger.feign.hystrix;

import com.java.yxt.feign.ResultObject;
import com.java.yxt.logger.feign.StaffOpLogFeign;
import com.java.yxt.logger.feign.entity.StaffOperationLog;
import org.springframework.stereotype.Component;

@Component
public class StaffOpLogFeignHystrix implements StaffOpLogFeign {

    @Override
    public ResultObject<Integer> postLog(StaffOperationLog staffOperationLog) {
        return null;
    }
}
