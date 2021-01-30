package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务标识po
 * @author zanglei
 */
@Data
@Slf4j
public class ServicePo extends  BasePo implements Serializable {
    /**
     *主键
     */
    private String id;

    /**
     *业务标识
     */
    private String serviceCode;

    /**
     *业务类型
     */
    private String serviceType;

    /**
     *客户代码
     */
    private String customerCode;

    /**
     *
     * 销售品编码
     */
    private String productId;

    /**
     *服务开始时间
     */
    private Date startTime;

    /**
     *服务结束时间
     */
    private Date endTime;
    /**
     *
     * 特服号
     */
    private String specialServiceNumber;

    /**
     *回调地址
     */
    private String callbackUrl;

    /**
     *
     *IP地址","隔开
     */
    private String platformIpList;

    /**
     *
     * 0 有效 1 无效
     */
    private Integer status;

    /**
     *
     * 创建时间
     */
    private Date createTime;

    /**
     *
     * 更新人ID
     */
    private String updaterId;

    /**
     *
     *更新时间
     */
    private Date updateTime;


    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getName());
        } catch (JsonProcessingException e) {
            log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
        }
        return null;
    }
}