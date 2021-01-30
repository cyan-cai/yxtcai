package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端Po
 * @author zanglei
 */
@Data
@Slf4j
public class TerminalPo extends  BasePo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *主键
     */
    private String id;
    /**
     * 用户名称
     */
    private String userName;

    /**
     *终端号码
     */
    private String msisdn;
    /**
     * url地址
     */
    private String url;

    /**
     *国际移动用户识别码
     */
    private String imsi;

    /**
     *行业
     */
    private Integer industry;

    /**
     * 终端类型
     */
    private Integer type;

    /**
     *用户状态
     */
    private Integer userStatus;

    /**
     *计费标识
     */
    private Integer charge;

    /**
     *数据来源
     */
    private Integer source;

    /**
     *客户ID
     */
    private String customerId;

    /**
     *0 有效 1 无效
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
     *更新时间
     */
    private Date updateTime;

    /**
     * 终端厂商
     */
    private String terminalFactory;

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