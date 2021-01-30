package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.TerminalPo;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * TerminalVo
 *
 * @author zanglei
 * @version V1.0
 * @description 终端po
 * @Package com.java.yxt.vo
 * @date 2020/9/15
 */
@Data
@Slf4j
public class TerminalVo extends TerminalPo implements Serializable {
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
     * 终端号码对应的客户名称
     */
    private String customerName;
    /**
     * 业务标识
     */
    private String serviceCode;
    /**
     * 客户id
     */
    private String customerId;
    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 终端id
     */
    private String terminalId;
    /**
     * 终端类型字符串
     */
    private String typeStr;
    /**
     * userstatus字符串
     */
    private String userStatusStr;
    /**
     * 数据来源字符串
     */
    private String sourceStr;
    /**
     * 行业类型字符串
     */
    private String industryStr;

    /**
     * 计费标识
     */
    private String chargeStr;

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
