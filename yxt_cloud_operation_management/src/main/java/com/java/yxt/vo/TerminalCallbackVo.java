package com.java.yxt.vo;

import com.java.yxt.po.TerminalCallbackPo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 回调关系表对应的bean
 * @author zanglei
 */
@Data
public class TerminalCallbackVo extends TerminalCallbackPo implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 终端号码列表
     */
    private List<String> msisdnList;
    /**
     * 业务标识表主键列表
     */
    private List<String> serviceIds;
    /**
     * api服务类型列表
     */
    private List<String> apiCategoryList;
    /**
     * 终端号码
     */
    private String msisdn;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户名称
     */
    private String customerCode;
    /**
     * 业务标识
     */
    private String serviceCode;
    /**
     * 回调地址
     */
    private String callBackUrl;
    /**
     * 起始页
     */
    private Integer current;
    /**
     * 每页显示记录数
     */
    private Integer size;
}