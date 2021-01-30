package com.java.yxt.service;

import java.util.Map;

/**
 * 接收协议适配服务消息处理
 */
public interface HanlderMqMessageService {

    /**
     * 计费标识请求处理
     * @param map
     */
    void shortdataChargingReq( Map<String, Object> map);

    /**
     * 短数据停复机处理
     * @param map
     */
    void shortDataChargeNotify(Map<String, Object> map);
}
