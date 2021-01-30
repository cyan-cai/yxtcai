package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端设备审核详情Po
 * @author 蔡家明
 */
@Data
@Slf4j
public class TerminalDeviceAuditDetailPo extends BasePo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *主键
     */
    private String id;

    /**
     *审核设备ID
     */
    private String auditId;

    /**
     *审核结果
     */
    private Integer auditResult;

    /**
     *审核人员
     */
    private String auditName;

    /**
     *审核时间
     */
    private Date auditTime;

    /**
     *备注
     */
    private String remark;

    /**
     *创建时间
     */
    private Date createTime;

    /**
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
