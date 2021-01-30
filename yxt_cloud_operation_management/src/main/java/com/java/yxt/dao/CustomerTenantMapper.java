package com.java.yxt.dao;

import com.java.yxt.vo.CustomerTenantVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerTenantMapper {
    /**
     * 查询
     * @param CustomerCode
     * @return
     */
    CustomerTenantVo selectByCustomerCode(@Param("customerCode") String CustomerCode);

    /**
     * 插入
     * @param customerTenantVo
     * @return
     */
    int insert (CustomerTenantVo customerTenantVo);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertList (List<CustomerTenantVo> list);
    /**
     * 更新
     * @param customerTenantVo
     * @return
     */
    int update(CustomerTenantVo customerTenantVo);

    /**
     * 删除
     * @param customerTenantVo
     * @return
     */
    int delete(CustomerTenantVo customerTenantVo);
}
