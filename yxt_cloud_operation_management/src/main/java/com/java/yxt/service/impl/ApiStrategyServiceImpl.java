package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.dao.ApiStrategyMapper;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.ApiStrategyService;
import com.java.yxt.vo.ApiStrategyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ApiStrategyServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description api策略处理
 * @Package com.java.yxt.service.impl
 * @date 2020/9/14
 */
@Service
public class ApiStrategyServiceImpl implements ApiStrategyService {

    @Autowired
    ApiStrategyMapper apiStrategyMapper;

    /**
     * 插入
     * @param apiStrategyVo
     * @return
     */
    @Override
    @SOLog(tableName = "mgt_api_strategy",remark = "添加api策略" ,type = OperationType.post)
    public long insert(ApiStrategyVo apiStrategyVo) {
        return apiStrategyMapper.insert(apiStrategyVo);
    }

    /**
     * 修改
     * @param apiStrategyVo
     * @return
     */
    @Override
    public int update(ApiStrategyVo apiStrategyVo) {
        return apiStrategyMapper.update(apiStrategyVo);
    }

    /**
     * 查询策略
     * @param apiStrategyVo
     * page
     * @return
     */
    @Override
    public List<ApiStrategyVo> select(Page page, ApiStrategyVo apiStrategyVo) {
        return apiStrategyMapper.select(page,apiStrategyVo);
    }
}
