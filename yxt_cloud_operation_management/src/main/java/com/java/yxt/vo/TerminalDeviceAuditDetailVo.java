package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.TerminalDeviceAuditDetailPo;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * TerminalDeviceAuditDetailVo
 *
 * @author 蔡家明
 * @version V1.0
 * @description 终端设备审核详情Vo
 * @Package com.java.yxt.vo
 * @date 2020/12/08
 */
@Data
@Slf4j
public class TerminalDeviceAuditDetailVo extends TerminalDeviceAuditDetailPo implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 国际移动设备识别码
     */
    private String imei;

    /**
     * 终端厂商
     */
    private String terminalFactory;

    /**
     * 审核状态
     */
    private Integer auditStatus;

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
