package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.TerminalDevicePo;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
@Data
@Slf4j
public class TerminalDeviceVo extends TerminalDevicePo implements Serializable {
    private static final long serialVersionUID = 2L;
    /**
     * 开始页
     */
    private Integer current;

    /**
     * 每页显示条数
     */
    private Integer size;

    /**
     * 终端类型字符串
     */
    private String typeStr;

    /**
     * 审核状态字符串
     */
    private String auditStatusStr;

    /**
     * 创建时间范围查询
     */
    private List<String> createTimeList;
    /**
     * 创建开始时间
     */
    private String createBeginTime;
    /**
     * 创建结束时间
     */
    private String createEndTime;

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
