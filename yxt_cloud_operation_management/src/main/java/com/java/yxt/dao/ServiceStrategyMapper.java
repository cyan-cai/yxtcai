package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.po.ServiceStrategyPo;
import com.java.yxt.vo.ServiceStrategyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户策略接口
 * @author   zanglei
 */
@Mapper
public interface ServiceStrategyMapper extends MyBaseMapper<ServiceStrategyPo>{
    /**
     * 删除
     * @param serviceStrategyVo
     * @return
     */
    int deleteByPrimaryKey(ServiceStrategyVo serviceStrategyVo);

    /**
     * 插入
     * @param serviceStrategyVo
     * @return
     */
    int insert(ServiceStrategyVo serviceStrategyVo);

    /**
     * 查询（分页）
     * @param page
     * @param serviceStrategyVo
     * @return
     */
    List<ServiceStrategyVo> select(Page<?> page, @Param("serviceStrategyVo") ServiceStrategyVo serviceStrategyVo);

    /**
     * 询不分页
     * @param serviceStrategyVo
     * @return
     */
    List<ServiceStrategyVo> selectAll(ServiceStrategyVo serviceStrategyVo);

    /**
     * 更新
     * @param serviceStrategyVo
     * @return
     */
    int update(ServiceStrategyVo serviceStrategyVo);

    /**
     * 启用禁用
     * @param serviceStrategyVo
     * @return
     */
    int enableDisable(ServiceStrategyVo serviceStrategyVo);



}