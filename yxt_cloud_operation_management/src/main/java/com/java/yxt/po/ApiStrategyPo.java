package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 策略po
 * @author zanglei
 */
@Data
@Slf4j
public class ApiStrategyPo extends  BasePo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *主键
     */
    private String id;

    /**
     *api id
     */
    private String apiId;

    /**
     * 每月上限限制
     */
    private Integer monthLimit;

    /**
     *每天限制
     */
    private Integer dayLimit;

    /**
     *每秒限制
     */
    private Integer secondLimit;

    /**
     * 每天开始时间
     */
    private String dayFrom;

    /**
     * 每天结束时间
     */
    private String dayTo;

    /**
     * 状态
     */
    private Integer status;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     * 修改人id
     */
    private String updaterId;

    /**
     *修改时间
     */
    private Date updateTime;


    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getName());
        } catch (JsonProcessingException e) {
            log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
        }
        return  null;
    }
}