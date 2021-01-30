package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.dao.ProductionMapper;
import com.java.yxt.dao.ServiceMapper;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.po.ProductionPo;
import com.java.yxt.service.ProductionService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.ProductionVo;
import com.java.yxt.vo.ServiceVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ProductionServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description 销售品实现类
 * @Package com.java.yxt.service.impl
 * @date 2020/9/10
 */
@Service
@Slf4j
public class ProductionServiceImpl implements ProductionService {

    @Autowired
    ProductionMapper productionMapper;
    @Autowired
    ServiceMapper serviceMapper;
    /**
     * 添加销售品
     * @param productionVo
     * @return
     */
    @Override
    @SetCreaterName
    @Transactional(rollbackFor = Exception.class)
    public int addProduction(ProductionVo productionVo) {
        productionVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        //销售品ID唯一性校验
        ProductionPo  productionPo=productionMapper.selectBySaleCode(productionVo);
        if(productionPo!=null){
            log.warn("销售品ID数据异常：{}，销售品ID已存在",productionVo.getSaleCode());
            throw new MySelfValidateException(ValidationEnum.PRODUCTION_SALECODE_EXISTS);
        }
        //销售品名称唯一性校验
        List<ProductionPo> list=productionMapper.selectByName(productionVo);
        if(ValidateUtil.isNotEmpty(list)){
            log.warn("销售品名称数据异常：{}，销售品名称已存在",productionVo.getName());
            throw new MySelfValidateException(ValidationEnum.PRODUCTION_NAME_EXISTS);
        }
        return productionMapper.add(productionVo);
    }

    /**
     * 更新销售品
     * @param productionVo
     * @return
     */
    @Override
    public int updateProduction(ProductionVo productionVo) {
        productionVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return productionMapper.update(productionVo);
    }

    /**
     * 查询销售品信息
     * @param productionVo
     * @return
     */
    @Override
    public List<ProductionPo> selectProduciton(Page page, ProductionVo productionVo) {
        if(ValidateUtil.isNotEmpty(productionVo.getCreateTimeList())){
            productionVo.setCreateBeginTime(productionVo.getCreateTimeList().get(0));
            productionVo.setCreateEndTime(productionVo.getCreateTimeList().get(1));
        }
        return productionMapper.selectProduction(page,productionVo);
    }

    /**
     * 不分页查询销售品信息
     * @param productionVo
     * @return
     */
    @Override
    public List<ProductionPo> selectAllProduciton(ProductionVo productionVo) {
        return productionMapper.selectAllProduction(productionVo);
    }

    /**
     * 查询所有关联API的商品
     * @param productionVo
     * @return
     */
    @Override
    public List<ProductionPo> selectAllValid(ProductionVo productionVo) {
        return productionMapper.selectAllValid(productionVo);
    }

    /**
     * 校验销售品
     * @param productionVo
     * @return
     */
    @Override
    public void checkProduction(ProductionVo productionVo) {
        productionVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        //销售品ID唯一性校验
        ProductionPo  productionPo=productionMapper.selectBySaleCode(productionVo);
        if(productionPo!=null){
            log.warn("销售品ID数据异常：{}，销售品ID已存在",productionVo.getSaleCode());
            throw new MySelfValidateException(ValidationEnum.PRODUCTION_SALECODE_EXISTS);
        }
        //销售品名称唯一性校验
        List<ProductionPo> list=productionMapper.selectByName(productionVo);
        if(ValidateUtil.isNotEmpty(list)){
            log.warn("销售品名称数据异常：{}，销售品名称已存在",productionVo.getName());
            throw new MySelfValidateException(ValidationEnum.PRODUCTION_NAME_EXISTS);
        }
    }

    /**
     * 销售品是否被订购校验
     * @param productionVo
     * @return
     */
    @Override
    public Response checkProductionIsOrdered(ProductionVo productionVo) {
        ServiceVo serviceVo=new ServiceVo();
        serviceVo.setProductId(productionVo.getSaleCode());
        List<ServiceVo> list=serviceMapper.selectBySaleCode(serviceVo);
        if(ValidateUtil.isNotEmpty(list)){
            return new Response(-1,"该销售品已被订购");
        }
        return Response.success();
    }
}
