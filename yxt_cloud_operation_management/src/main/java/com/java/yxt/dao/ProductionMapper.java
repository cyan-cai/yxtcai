package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.po.ProductionPo;
import com.java.yxt.vo.ProductionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 销售品dao层
 * @author  zanglei
 */
@Mapper
public interface ProductionMapper extends MyBaseMapper<ProductionPo>{

    /**
     * 添加销售品
     * @param productionVo
     * @return
     */
     int add(ProductionVo productionVo);

    /**
     * 更新销售品
     * @param productionVo
     * @return
     */
     int update(ProductionVo productionVo);

    /**
     * 查询销售品信息
     * @param productionVo
     * @param page
     * @return
     */
     List<ProductionPo> selectProduction(Page<?> page, @Param("productionVo") ProductionVo productionVo);

    /**
     * 不分页查询
     * @param productionVo
     * @return
     */
     List<ProductionPo> selectAllProduction(ProductionVo productionVo);

    /**
     * 查询所有关联API的商品
     * @param productionVo
     * @return
     */
    List<ProductionPo> selectAllValid(ProductionVo productionVo);


    /**
     * 根据销售ID查询销售品信息
     * @param productionVo
     * @return
     */
     ProductionPo selectBySaleCode(ProductionVo productionVo);

    /**
     * 根据销售名称查询销售品信息
     * @param productionVo
     * @return
     */
    List<ProductionPo> selectByName(ProductionVo productionVo);
}
