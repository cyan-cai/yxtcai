package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端设备Po
 * @author 蔡家明
 */
@Data
@Slf4j
public class TerminalDevicePo extends  BasePo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *主键
     */
    private String id;

    /**
     * 国际移动设备识别码
     */
    private String imei;

    /**
     * 终端类型
     */
    private Integer type;

    /**
     * 终端厂商
     */
    private String terminalFactory;

    /**
     * 终端型号
     */
    private String model;

    /**
     * 移动设备识别码
     */
    private String meid;

    /**
     * 审核状态
     */
    private Integer auditStatus;

    /**
     *创建时间
     */
    private Date createTime;

    /**
     *更新时间
     */
    private Date updateTime;

    /**
     * 终端设备状态 1正常 0已删除
     */
    private Integer status;

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
