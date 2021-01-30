package com.java.yxt.logger.feign.hystrix;

import com.java.yxt.feign.ResultObject;
import com.java.yxt.logger.feign.SysAlarmLogFeign;
import com.java.yxt.logger.feign.entity.SysAlarmLog;

public class SysAlarmLogFeignHystrix implements SysAlarmLogFeign {

    @Override
    public ResultObject<Integer> postLog(SysAlarmLog sysAlarmLog) {
        return null;
    }
}
