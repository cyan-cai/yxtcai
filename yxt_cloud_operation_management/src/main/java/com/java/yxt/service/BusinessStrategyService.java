package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.ServiceStrategyVo;

import java.util.List;

/**
 * 客户策略处理
 * @author zanglei
 */
public interface BusinessStrategyService {
    /**
     * 插入
     * @param serviceStrategyVo
     * @return
     */
    int insert(ServiceStrategyVo serviceStrategyVo);

    /**
     * 分页查询
     * @param page
     * @param serviceStrategyVo
     * @return
     */
    List<ServiceStrategyVo> select(Page<?> page, ServiceStrategyVo serviceStrategyVo);

    /**
     * 修改
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


    /**
     * 不分页查询
     * @param serviceStrategyVo
     * @return
     */
    public List<ServiceStrategyVo> selectAll(ServiceStrategyVo serviceStrategyVo);
}
