package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.ApiCatalogVo;
import com.java.yxt.vo.ApiVo;

import java.util.List;

/**
 * api目录管理模块
 * @author zanglei
 */
public interface ApiCatalogService {
    /**
     * 插入
     * @param apiCatalogVo
     * @return
     */
    long insert(ApiCatalogVo apiCatalogVo);

    /**
     * 删除
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 更新
     * @param apiCatalogVo
     * @return
     */
    int update(ApiCatalogVo apiCatalogVo);

    /**
     * 查询
     * @param apiCatalogVo
     * @param page
     * @return
     */
    List<ApiCatalogVo> select(Page page ,ApiCatalogVo apiCatalogVo);

    /**
     * 不分页查询
     * @param apiCatalogVo
     * @return
     */
    List<ApiCatalogVo> selectAll(ApiCatalogVo apiCatalogVo);

    /**
     * 查询api目录树结构
     * @param apiCatalogVo
     * @return
     */
    List<ApiCatalogVo> selectCatalogTree(ApiCatalogVo apiCatalogVo);

    /**
     * 查询
     * @param apiVo
     * @param page
     * @return
     */
    List<ApiVo> selectApiByCatalog(Page page,ApiVo apiVo);

    /**
     * api目录关联的api数量
     * @param apiVo
     * @return
     */
    Integer selectApiCountByCatalog(ApiVo apiVo);

}
