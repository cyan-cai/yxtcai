package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.dto.CustomerDto;
import com.java.yxt.po.CustomerPo;
import com.java.yxt.vo.CustomerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 客户管理dao层
 * @author zanglei
 */
@Mapper
public interface CustomerMapper extends MyBaseMapper<CustomerPo>{
    /**
     * 删除
     * @param customerVo
     * @return
     */
    int deleteByPrimaryKey(CustomerVo customerVo);

    /**
     * 插入
     * @param customerVo
     * @return
     */
    @SetCreaterName
    int insert(CustomerVo customerVo);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertList(List<CustomerVo> list);

    /**
     * 分页查询
     * @param page
     * @param customerVo
     * @return
     */
    List<CustomerVo> select(Page<CustomerVo> page,@Param("customerVo") CustomerVo customerVo);


    /**
     * 查询
     * @param customerVo
     * @return
     */
    List<CustomerVo> selectAll(@Param("customerVo")CustomerVo customerVo);

    /**
     * 精确查询
     * @param customerVo
     * @return
     */
    CustomerVo selectCustomerByCustomerCode(@Param("customerVo")CustomerVo customerVo);

    /**
     * 精确查询
     * @param customerVo
     * @return
     */
    CustomerVo selectCustomerByCustomerId(@Param("customerVo")CustomerVo customerVo);

    /**
     * 精确查询
     * @param customerVo
     * @return
     */
    CustomerVo selectCustomerByCustomerName(@Param("customerVo")CustomerVo customerVo);

    /**
     * 精确查询
     * @param customerVo
     * @return
     */
    CustomerVo selectCustomerByCustomerCodeAndCustomerName(@Param("customerVo")CustomerVo customerVo);
    /**
     * 更新
     * @param customerVo
     * @return
     */
    int update(CustomerVo customerVo);
    /**
     * 更新客户ID
     * @param customerVo
     * @return
     */
    int updateId(CustomerVo customerVo);
    /**
     * 更新客户状态
     * @param customerVo
     * @return
     */
    int updateStatus(@Param("customerVo")CustomerVo customerVo);
    /**
     * 根据终端号码分页查询客户信息
     * @param page
     * @param customerDto
     * @return
     */
    List<CustomerDto> selectByMsisdn(Page<?> page,@Param("customerDto") CustomerDto customerDto);
    /**
     * 根据终端号码分页查询客户信息
     * @param page
     * @param customerDto
     * @return
     */
    List<CustomerVo> selectCommon(Page<?> page,@Param("customerDto") CustomerDto customerDto);

    /**
     * 根据特服号查询客户
     * @param customerCodes
     * @return
     */
    List<CustomerVo> selectCustomerByCustomerCodes(@Param("customerCodes")Set customerCodes);
}