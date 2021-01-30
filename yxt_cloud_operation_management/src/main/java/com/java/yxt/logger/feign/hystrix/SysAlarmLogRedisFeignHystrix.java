package com.java.yxt.logger.feign.hystrix;

import com.java.yxt.feign.ResultObject;
import com.java.yxt.logger.feign.SysAlarmLogRedisFeign;
import com.java.yxt.logger.feign.entity.SysAlarmLog;

public class SysAlarmLogRedisFeignHystrix implements SysAlarmLogRedisFeign {
    @Override
    public ResultObject<Integer> postLog(SysAlarmLog sysAlarmLog) {
        return null;
    }
}
