package com.java.yxt.service;

import com.java.yxt.vo.CustomerTenantVo;

/**
 * 客户与租户关系管理
 * @author caijiaming
 */
public interface CustomerTenantService {

    /**
     * 查询
     * @param customerCode
     * @return
     */
    CustomerTenantVo selectByCustomerCode(String customerCode);
}
