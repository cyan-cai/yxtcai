package com.java.yxt.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * CustomerTenantVo
 *
 * @author caijiaming
 * @version V1.0
 * @description 客户与租户关系vo，供外部调用使用
 * @Package com.java.yxt.vo
 * @date 2020/11/17
 */
@Data
public class CustomerTenantVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 租户ID
     */
    private String tenantId;
}

