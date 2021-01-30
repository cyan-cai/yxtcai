package com.java.yxt.service;

import com.java.yxt.vo.ProductionApiRelationVo;

import java.util.List;

/**
 * 销售品和api关联接口
 * @author zanglei
 */
public interface ProductionApiRelationService {

     /** 添加销售品关联api
     * @param productionApiRelationVo
     * @return
      **/
    int add(ProductionApiRelationVo productionApiRelationVo);

    /**
     * 查询销售品关联api信息
     * @param productionApiRelationVo
     * @return
     */
    List<ProductionApiRelationVo> select(ProductionApiRelationVo productionApiRelationVo);

    /**
     * 删除销售品和api关联关系
     * @param productionApiRelationVo
     * @return
     */
    int delete(ProductionApiRelationVo productionApiRelationVo);
}
