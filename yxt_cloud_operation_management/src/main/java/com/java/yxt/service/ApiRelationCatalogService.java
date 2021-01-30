package com.java.yxt.service;

import com.java.yxt.vo.ApiRelationCatalogVo;

/**
 * api和目录关系接口
 * @author zanglei
 */
public interface ApiRelationCatalogService {
    /**
     * 插入
     * @param apiRelationCatalogVo
     * @return
     */
    int insert(ApiRelationCatalogVo apiRelationCatalogVo);

    /**
     * 删除
     * @param apiRelationCatalogVo
     * @return
     */
    int delete(ApiRelationCatalogVo apiRelationCatalogVo);

    /**
     * 目录和api解除绑定
     * @param apiRelationCatalogVo
     * @return
     */
    int unRelation(ApiRelationCatalogVo apiRelationCatalogVo);

    /**
     * 更新
     * @param apiRelationCatalogVo
     * @return
     */
    int update(ApiRelationCatalogVo apiRelationCatalogVo);
}
