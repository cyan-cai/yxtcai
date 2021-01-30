package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端黑名单po
 * @author zanglei
 */
@Data
@Slf4j
public class TerminalBlackPo extends  BasePo implements Serializable {
    /**
     *
     * 主键
     */
    private String id;

    /**
     *
     * 限制级别 客户级，用户级（用户系统级，用户行业级）
     */
    private String level;

    /**
     *
     * 客户业务标识
     */
    private String serviceCode;

    /**
     *
     * 终端id
     */
    private String terminalId;
    /**
     * 客户代码
     */
    private String customerCode;

    /**
     *
     * 主被叫类型
     */
    private String type;

    /**
     *状态
     */
    private Integer status;

    /**
     *状态字符串
     */
    private String statusStr;

    /**
     *
     * 创建时间
     */
    private Date createTime;

    /**
     *
     * 更新人id
     */
    private String updaterId;

    /**
     *
     * 更新时间
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