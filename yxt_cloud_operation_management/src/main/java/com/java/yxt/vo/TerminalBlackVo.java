package com.java.yxt.vo;

import com.java.yxt.po.TerminalBlackPo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * TerminalBlackVo
 *
 * @author zanglei
 * @version V1.0
 * @description 终端黑名单vo
 * @Package com.java.yxt.vo
 * @date 2020/9/19
 */
@Data
public class TerminalBlackVo extends TerminalBlackPo implements Serializable {

    private static final long serialVersionUID = 2L;
    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页条数
     */
    private Integer size;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户黑名单的业务标识列表
     */
    private List<String> serviceCodes;

    /**
     * msisdn
     */
    private String msisdn;
    
    /**
     * 查询使用，绑定时间查询
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
    /**
     * 客户ID
     */
    private String customerId;
}
