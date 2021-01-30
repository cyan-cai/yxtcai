package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author zanglei
 * @version V1.0
 * @description ip地址
 * @Package com.java.yxt.vo
 * @date 2020/9/28
 */
@Data
@Slf4j
public class IPVo implements Serializable {

    private static final long serialVersionUID = 2L;


    /**
     *
     * ip 协议 IPV4,IPV6
     */
    private String ipProtocal;

    /**
     *IP地址
     */
    private String ip;

    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getSimpleName());
        } catch (JsonProcessingException e) {
           log.error("对象：{}转jsonString异常：{}",this.getClass().getSimpleName(),e.getMessage());
        }
        return null;
    }
}
