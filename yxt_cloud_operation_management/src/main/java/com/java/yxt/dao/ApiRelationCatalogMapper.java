package com.java.yxt.dao;

import com.java.yxt.po.ApiRelationCatalogPo;
import com.java.yxt.vo.ApiRelationCatalogVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * api和目录关系dao层接口
 * @author zanglei
 */
@Mapper
public interface ApiRelationCatalogMapper extends MyBaseMapper<ApiRelationCatalogPo>{
    /**
     * 删除
     * @param apiRelationCatalogVo
     * @return
     */
    int delete(ApiRelationCatalogVo apiRelationCatalogVo);

    /**
     * 解除绑定
     * @param apiRelationCatalogVo
     * @return
     */
    int unRelation(ApiRelationCatalogVo apiRelationCatalogVo);

    /**
     * 插入
     * @param apiRelationCatalogVo
     * @return
     */
    int insert(ApiRelationCatalogVo apiRelationCatalogVo);

    /**
     * 分页查询
     * @param apiRelationCatalogVo
     * @return
     */
    List<ApiRelationCatalogVo> select(ApiRelationCatalogVo apiRelationCatalogVo);

    /**
     * 根据主键更新
     * @param apiRelationCatalogVo
     * @return
     */
    int update(ApiRelationCatalogVo apiRelationCatalogVo);
}