package com.java.yxt.logger.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.java.yxt.logger.feign.entity.ApiInVokeLog;

@FeignClient(value = "oamsvc")
public interface ApiInvokeLogRedisFeign {
    String API ="/service/112/ailRApi";


    /**
     * 插入api接口调用日志信息
     * @param apiInVokeLog
     * @return
     */
    @PostMapping(API+"/postLog")
    public void postLog(ApiInVokeLog apiInVokeLog);


}
