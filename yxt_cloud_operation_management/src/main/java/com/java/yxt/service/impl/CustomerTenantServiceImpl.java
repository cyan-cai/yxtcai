package com.java.yxt.service.impl;

import com.java.yxt.dao.CustomerTenantMapper;
import com.java.yxt.service.CustomerTenantService;
import com.java.yxt.vo.CustomerTenantVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class CustomerTenantServiceImpl implements CustomerTenantService {

    @Autowired
    CustomerTenantMapper customerTenantMapper;
    @Override
    public CustomerTenantVo selectByCustomerCode(String customerCode) {
        return customerTenantMapper.selectByCustomerCode(customerCode);
    }
}
