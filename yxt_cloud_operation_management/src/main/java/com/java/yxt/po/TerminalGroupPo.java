package com.java.yxt.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.BasePo;
import com.java.yxt.util.JsonUtil;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author Jiang Liang
 * @version 1.0.0
 * @time 2021-01-18 15:20
 */
@Slf4j
@Data
public class TerminalGroupPo extends BasePo {
    private String id;

    /**
     * 有效标识 0-有效 ，1-无效
     */
    private Integer validStatus;

    /**
     * 组织名称
     */
    private String groupDesc;

    /**
     * 组织名称
     */
    private String groupName;

    /**
     * 终端类型
     */
    private Integer terminalType;

    /**
     * 客户ID
     */
    @ApiModelProperty(hidden=true)
    private String customerId;

    /**
     *
     * 创建时间
     */
    @ApiModelProperty(hidden=true)
    private Date createTime;

    /**
     *
     *更新时间
     */
    @ApiModelProperty(hidden=true)
    private Date updateTime;

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
