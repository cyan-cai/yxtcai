package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.BasePo;
import com.java.yxt.po.TerminalGroupPo;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author Jiang Liang
 * @version 1.0.0
 * @time 2021-01-19 10:54
 */
@Data
@Slf4j
public class TerminalGroupMidPo extends BasePo {

    private String id;

    /**
     *
     * 分组表主键
     */
    private String groupId;

    /**
     *
     * 设备表主键
     */
    private String terminalId;

    /**
     *
     * 创建时间
     */
    private Date createTime;

    /**
     *
     *更新时间
     */
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
