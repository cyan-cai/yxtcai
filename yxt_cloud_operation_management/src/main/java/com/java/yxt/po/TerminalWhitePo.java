package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端白名单po
 * @author zanglei
 */
@Data
@Slf4j
public class TerminalWhitePo extends  BasePo implements Serializable {
    /**
     *主键
     */
    private String id;

    /**
     *业务标识id
     */
    private String serviceCode;

    /**
     *终端id
     */
    private String terminalId;

    /**
     *
     * 状态 1 有效 0 无效
     */
    private Integer status;

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