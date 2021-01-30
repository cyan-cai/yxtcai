package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.dao.ApiCatalogMapper;
import com.java.yxt.exception.MySelfValidateException;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.ApiCatalogService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.ApiCatalogVo;
import com.java.yxt.vo.ApiVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ApiCatalogServiceImpl
 *
 * @author zanglei
 * @version V1.0
 * @description &lt;文件描述&gt;
 * @Package com.java.yxt.service.impl
 * @date 2020/9/14
 */
@Service
@Slf4j
public class ApiCatalogServiceImpl implements ApiCatalogService {


    @Autowired
    ApiCatalogMapper apiCatalogMapper;

    /**
     * 插入
     * @param apiCatalogVo
     * @return
     */
    @Override
    public long insert(ApiCatalogVo apiCatalogVo) {
        apiCatalogVo.setCreateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        // 判断api目录是否已存在
        ApiCatalogVo apiCatalogVo1 = new ApiCatalogVo();
        apiCatalogVo1.setName(apiCatalogVo.getName());
        List<ApiCatalogVo> apiCatalogVo2 = apiCatalogMapper.selectByIndex(apiCatalogVo1);
        if(ValidateUtil.isNotEmpty(apiCatalogVo2) && apiCatalogVo2.size()>0){
            log.error("api目录：{},已存在",apiCatalogVo1.getName());
            throw new MySelfValidateException(ValidationEnum.API_CATALOG_NAME_EXISTS);
        }
        return apiCatalogMapper.insert(apiCatalogVo);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    @SOLog(type = OperationType.delete,tableName = "mgt_api_catalog",remark = "删除api目录",mapperName = "apiCatalogMapper",
            paramKey = "id")
    public int delete(Long id) {
        return apiCatalogMapper.deleteByPrimaryKey(String.valueOf(id));
    }

    /**
     * 更新
     * @param apiCatalogVo
     * @return
     */
    @Override
    public int update(ApiCatalogVo apiCatalogVo) {
        apiCatalogVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
        // 判断api目录是否已存在
        ApiCatalogVo apiCatalogVo1 = new ApiCatalogVo();
        apiCatalogVo1.setName(apiCatalogVo.getName());
        apiCatalogVo1.setId(apiCatalogVo.getId());
        List<ApiCatalogVo> apiCatalogVo2 = apiCatalogMapper.selectByIndex(apiCatalogVo1);
        if(ValidateUtil.isNotEmpty(apiCatalogVo2) && apiCatalogVo2.size()>0){
            log.error("api目录：{},已存在",apiCatalogVo1.getName());
            throw new MySelfValidateException(ValidationEnum.API_CATALOG_NAME_EXISTS);
        }
        return apiCatalogMapper.updateByPrimaryKey(apiCatalogVo);
    }

    /**
     * 分页
     * @param apiCatalogVo
     * @return
     */
    @Override
    public List<ApiCatalogVo> select(Page page, ApiCatalogVo apiCatalogVo) {
        return apiCatalogMapper.select(page,apiCatalogVo);
    }

    /**
     * 不分页
     * @param apiCatalogVo
     * @return
     */
    @Override
    public List<ApiCatalogVo> selectAll(ApiCatalogVo apiCatalogVo) {
        return apiCatalogMapper.selectAll(apiCatalogVo);
    }

    /**
     * 查询api目录树结构
     * @param apiCatalogVo
     * @return
     */
    @Override
    public List<ApiCatalogVo> selectCatalogTree(ApiCatalogVo apiCatalogVo) {
        return apiCatalogMapper.selectCatalogTree(apiCatalogVo);
    }

    /**
     * 根据目录查询对应的api信息
     * @param apiVo
     * @return
     */
    @Override
    public List<ApiVo> selectApiByCatalog(Page page,ApiVo apiVo) {
        return apiCatalogMapper.selectApiByCatalog(page,apiVo);
    }

    /**
     * api目录关联的api数量
     * @param apiVo
     * @return
     */
    @Override
    public Integer selectApiCountByCatalog(ApiVo apiVo) {
        return apiCatalogMapper.selectApiCountByCatalog(apiVo);
    }



}
