package com.java.yxt.vo;

import com.java.yxt.po.ServiceStrategyPo;
import lombok.Data;

import java.io.Serializable;

/**
 * ServiceStrategyVo
 *
 * @author zanglei
 * @version V1.0
 * @description 客户策略
 * @Package com.java.yxt.vo
 * @date 2020/9/16
 */
@Data
public class ServiceStrategyVo extends ServiceStrategyPo implements Serializable {

    private static final long serialVersionUID = 2L;


    /**
     * 业务标识
     */
    private String serviceCode;

    /**
     * 客户名称
     *
     */
    private String customerName;
    /**
     * 客户号码
     */
    private String phoneNum;

    /**
     * 当前页
     */
    private Integer current;
    /**
     * 显示条数
     */
    private Integer size;
}
