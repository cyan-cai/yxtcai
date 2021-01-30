package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zanglei
 * @version V1.0
 * @description 客户实体类
 * @Package com.java.yxt.po
 * @date 2020/10/22
 */
@Data
@Slf4j
public class CustomerPo extends BasePo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *主键
     */
    private String id;

    /**
     *客户代码
     */
    private String customerCode;
    /**
     * 客户业务代码
     */
    private String customerBusinessCode;

    /**
     *客户名称
     */
    private String name;

    /**
     *ip地址
     */
    private String ip;

    /**
     * 计费标识 1计费，0不计费
     */
    private Integer charge;

    /**
     *电话号码
     */
    private String phoneNum;

    /**
     *email
     */
    private String email;

    /**
     *属性
     */
    private String type;

    /**
     *权限 2、周期3、紧急定位1、单次
     */
    private Integer limit;

    /**
     *客户状态 1、商用2、试商用3、测试
     */
    private Integer customerStatus;

    /**
     *接入方式
     */
    private String serviceWay;

    /**
     * 优先级
     */
    private Integer level;

    /**
     *优先级顺序
     */
    private String levelSeq;

    /**
     * 1开通，0不开通
     */
    private Integer saas;

    /**
     * 0 有效 1 无效
     */
    private Integer status;
    /**
     *创建时间
     */
    private Date createTime;

    /**
     *更新人ID
     */
    private String updaterId;

    /**
     *
     * 更新时间
     */
    private Date updateTime;

    /**
     * 行业
     */
    private String industryList;

    /**
     * 特服号
     */
    private String srcTermId;

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
