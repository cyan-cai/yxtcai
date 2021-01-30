package com.java.yxt.logger.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.java.yxt.feign.ResultObject;
import com.java.yxt.logger.feign.entity.ApiInVokeLog;
import com.java.yxt.logger.feign.hystrix.ApiInvokeLogFeignHystrix;

@FeignClient(value = "oamsvc",fallback = ApiInvokeLogFeignHystrix.class)
public interface ApiInvokeLogFeign {
    String API ="/service/112/ailApi";

    /**
     * 插入api接口调用日志信息
     * @param apiInVokeLog
     * @return
     */
    @PostMapping(API)
    public ResultObject postLog(ApiInVokeLog apiInVokeLog);
}
