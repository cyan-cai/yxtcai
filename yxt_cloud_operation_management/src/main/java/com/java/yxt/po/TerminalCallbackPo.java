package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端回调关系po
 * @author zanglei
 */
@Data
@Slf4j
public class TerminalCallbackPo extends BasePo implements Serializable {
    /**
     *主键
     */
    private String id;

    /**
     * 终端号码id
     */
    private String terminalId;

    /**
     *
     * 服务类型（配置表code）
     */
    private String apiCategory;

    /**
     *业务标识ID
     */
    private String serviceId;

    /**
     *0 有效 1 无效
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
     *更新时间
     */
    private Date updateTime;


    private static final long serialVersionUID = 1L;


    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getName());
        } catch (JsonProcessingException e) {
           log.error("{}对象转jsonstring失败：{}",this.getClass().getSimpleName(),e.getMessage());
        }
        return null;
    }
}