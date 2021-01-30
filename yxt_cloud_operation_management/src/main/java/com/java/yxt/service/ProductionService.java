package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.po.ProductionPo;
import com.java.yxt.vo.ProductionVo;

import java.util.List;

/**
 *销售品服务接口
 * @author zanglei
 */
public interface ProductionService {

    /**
     * 添加销售品
     * @param productionVo
     * @return
     */
    public int addProduction(ProductionVo productionVo);

    /**
     * 更新销售品
     * @param productionVo
     * @return
     */
    public int updateProduction(ProductionVo productionVo);

    /**
     * 查询销售品信息
     * @param productionVo
     * @param page
     * @return
     */
    public List<ProductionPo> selectProduciton(Page<?> page, ProductionVo productionVo);

    /**
     * 不分页查询销售品信息
     * @param productionVo
     * @return
     */
     List<ProductionPo> selectAllProduciton(ProductionVo productionVo);

    /**
     * 查询所有关联API的商品
     * @param productionVo
     * @return
     */
    List<ProductionPo> selectAllValid(ProductionVo productionVo);

    /**
     * 校验销售品
     * @param productionVo
     * @return
     */
    public void checkProduction(ProductionVo productionVo);

    /**
     * 销售品是否被订购校验
     * @param productionVo
     * @return
     */
    Response checkProductionIsOrdered(ProductionVo productionVo);
}
