package com.java.yxt.service.impl;

import com.java.yxt.dao.ApiRelationCatalogMapper;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.ApiRelationCatalogService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.vo.ApiRelationCatalogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * ApiRelationCatalogServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description api和目录关系处理
 * @Package com.java.yxt.service.impl
 * @date 2020/9/14
 */
@Service
public class ApiRelationCatalogServiceImpl implements ApiRelationCatalogService {

    @Autowired
    ApiRelationCatalogMapper apiRelationCatalogMapper;

    @Override
    public int insert(ApiRelationCatalogVo apiRelationCatalogVo) {
        apiRelationCatalogVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        return apiRelationCatalogMapper.insert(apiRelationCatalogVo);
    }

    @Override
    public int delete(ApiRelationCatalogVo apiRelationCatalogVo) {
        return apiRelationCatalogMapper.delete(apiRelationCatalogVo);
    }

    @Override
    @SOLog(type = OperationType.put,tableName = "mgt_api_relation_catalog" ,mapperName = "apiRelationCatalogMapper",
    remark = "API目录下解绑API",paramKey = "apiId")
    public int unRelation(ApiRelationCatalogVo apiRelationCatalogVo) {
        return apiRelationCatalogMapper.unRelation(apiRelationCatalogVo);
    }

    @Override
    public int update(ApiRelationCatalogVo apiRelationCatalogVo) {
        return apiRelationCatalogMapper.update(apiRelationCatalogVo);
    }
}
