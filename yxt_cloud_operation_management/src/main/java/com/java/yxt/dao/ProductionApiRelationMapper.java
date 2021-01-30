package com.java.yxt.dao;

import com.java.yxt.po.ProductionApiRelationPo;
import com.java.yxt.vo.ProductionApiRelationVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 销售品和api关系dao层
 * @author zanglei
 */
@Mapper
public interface ProductionApiRelationMapper extends MyBaseMapper<ProductionApiRelationPo>{

    /**
     * 添加销售品关联api
     * @param productionApiRelationVo
     * @return
     */
     int add(ProductionApiRelationVo productionApiRelationVo);

//    /**
//     * 更新销售品关联api
//     * @param productionVo
//     * @return
//     */
//    public int update(ProductionApiRelationVo productionVo);

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
