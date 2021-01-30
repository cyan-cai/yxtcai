package com.java.yxt.service.impl;

import com.java.yxt.dao.ProductionApiRelationMapper;
import com.java.yxt.service.ProductionApiRelationService;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.ProductionApiRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * ProductionApiRelationServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description 销售品和api关联服务层处理
 * @Package com.java.yxt.service.impl
 * @date 2020/9/11
 */
@Service
public class ProductionApiRelationServiceImpl implements ProductionApiRelationService {
    @Autowired
    ProductionApiRelationMapper productionApiRelationMapper;

    /**
     *添加销售品api关联
     * @param productionApiRelationVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(ProductionApiRelationVo productionApiRelationVo) {
        // 先删除再插入
        int deleteCount = productionApiRelationMapper.delete(productionApiRelationVo);
        // 判断是否是全部取消
        if(ValidateUtil.isEmpty(productionApiRelationVo.getApiIds())){
            return 0;
        }
        return productionApiRelationMapper.add(productionApiRelationVo);
    }

    /**
     * 查询销售品api关联
     * @param productionApiRelationVo
     * @return
     */
    @Override
    public List<ProductionApiRelationVo> select(ProductionApiRelationVo productionApiRelationVo) {
        return productionApiRelationMapper.select(productionApiRelationVo);
    }

    /**
     * 根据id删除销售品api关联
     * @param productionApiRelationVo
     * @return
     */
    @Override
    public int delete(ProductionApiRelationVo productionApiRelationVo) {
        return productionApiRelationMapper.delete(productionApiRelationVo);
    }
}
