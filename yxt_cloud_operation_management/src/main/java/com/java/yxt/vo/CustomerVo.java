package com.java.yxt.vo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import com.java.yxt.po.CustomerPo;
import java.io.Serializable;

/**
 * CustomerVo
 *
 * @author zanglei
 * @version V1.0
 * @description 客户vo
 * @Package com.java.yxt.vo
 * @date 2020/9/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerVo extends CustomerPo implements Serializable {

    private static final long serialVersionUID = 2L;
    /**
     * 回调地址
     */
    private String callbackUrl;
    /**
     * 业务标识
     */
    private String serviceCode;
    /**
     *服务类型
     */
    private String apiCategory;

    /**
     * 客户编码
     */
    private String customerCode;
    /**
     *
     *终端号码
     **/
    private String msisdn;
    /**
     * 业务标识表主键
     */
    private String serviceId;
    /**
     * 渠道
     */
    private String channel;
    /**
     * 客户权限中文
     */
    private String limitStr;
    /**
     * 客户业务状态中文
     */
    private String customerStatusStr;
    /**
     * 客户状态中文
     */
    private String StatusStr;
    /**
     * 业务优先级中文
     */
    private String levelStr;
    /**
     * 开始页
     */
    private Integer current;

    /**
     * 每页显示条数
     */
    private Integer size;

}
