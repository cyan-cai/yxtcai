package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.dao.ServiceStrategyMapper;
import com.java.yxt.service.BusinessStrategyService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.vo.ServiceStrategyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * BusinessStrategyServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description 客户策略处理实现类
 * @Package com.java.yxt.service.impl
 * @date 2020/9/17
 */
@Service
public class BusinessStrategyServiceImpl implements BusinessStrategyService {

    @Autowired
    ServiceStrategyMapper serviceStrategyMapper;

    /**
     * 插入
     * @param serviceStrategyVo
     * @return
     */
    @Override
    public int insert(ServiceStrategyVo serviceStrategyVo) {
        return serviceStrategyMapper.insert(serviceStrategyVo);
    }

    /**
     * 查询
     * @param page
     * @param serviceStrategyVo
     * @return
     */
    @Override
    public List<ServiceStrategyVo> select(Page<?> page, ServiceStrategyVo serviceStrategyVo) {
        return serviceStrategyMapper.select(page,serviceStrategyVo);
    }

    /**
     * 查询
     * @param serviceStrategyVo
     * @return
     */
    @Override
    public List<ServiceStrategyVo> selectAll(ServiceStrategyVo serviceStrategyVo) {
        return serviceStrategyMapper.selectAll(serviceStrategyVo);
    }

    /**
     * 更新
     * @param serviceStrategyVo
     * @return
     */
    @Override
    public int update(ServiceStrategyVo serviceStrategyVo) {
        return serviceStrategyMapper.update(serviceStrategyVo);
    }

    /**
     * 启用禁用
     * @param serviceStrategyVo
     * @return
     */
    @Override
    public int enableDisable(ServiceStrategyVo serviceStrategyVo) {
        serviceStrategyVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return serviceStrategyMapper.enableDisable(serviceStrategyVo);
    }
}
