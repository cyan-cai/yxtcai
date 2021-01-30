package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.po.ApiStrategyPo;
import com.java.yxt.vo.ApiStrategyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 策略dao
 * @author zanglei
 */
@Mapper
public interface ApiStrategyMapper extends MyBaseMapper<ApiStrategyPo>{
    /**
     * 删除
     * @param apiStrategyVo
     * @return
     */
    int delete(ApiStrategyVo apiStrategyVo);

    /**
     * 插入
     * @param apiStrategyVo
     * @return
     */
    long insert(ApiStrategyVo apiStrategyVo);

    /**
     * 查询
     * @param page
     * @param apiStrategyVo
     * @return
     */
    List<ApiStrategyVo> select( Page<ApiStrategyVo> page, @Param("apiStrategyVo") ApiStrategyVo apiStrategyVo);

    /**
     * 修改
     * @param apiStrategyVo
     * @return
     */
    int update(ApiStrategyVo apiStrategyVo);
}