package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.ApiStrategyVo;

import java.util.List;

/**
 * api 策略
 * @author zanglei
 */
public interface ApiStrategyService {
    /**
     * 插入
     * @param apiStrategyVo
     * @return
     */
    long insert(ApiStrategyVo apiStrategyVo);

    /**
     * 更新
     * @param apiStrategyVo
     * @return
     */
    int update(ApiStrategyVo apiStrategyVo);

    /**
     * 分页查询
     * @param page
     * @param strategyVo
     * @return
     */
    List<ApiStrategyVo> select (Page<?> page,ApiStrategyVo strategyVo);
}
