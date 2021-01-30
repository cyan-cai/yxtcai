package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户策略po
 * @author zanglei
 */
@Data
@Slf4j
public class ServiceStrategyPo extends  BasePo implements Serializable {
    /**
     *主键
     */
    private String id;

    /**
     *业务标识id
     */
    private String serviceId;

    /**
     *每秒上限
     */
    private Integer secondLimit;

    /**
     *每天上限
     */
    private Integer dayLimit;

    /**
     *每月上限
     */
    private Integer monthLimit;

    /**
     *
     * 每天开始时间“09:00”
     */
    private String dayFrom;

    /**
     *
     *每天结束时间“22:00”
     */
    private String dayTo;

    /**
     * 0 有效 1 无效
     */
    private Integer status;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *
     * 更新人ID
     */
    private String updaterId;

    /**
     *更新时间
     */
    private Date updateTime;


    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getName());
        } catch (JsonProcessingException e) {
           log.error("{}对象转jsonString失败：{}",this.getClass().getSimpleName(),e.getMessage());
        }
        return null;
    }
}