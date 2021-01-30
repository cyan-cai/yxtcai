package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.po.ApiCatalogPo;
import com.java.yxt.vo.ApiCatalogVo;
import com.java.yxt.vo.ApiVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * api目录接口
 * @author zanglei
 */
@Mapper
public interface ApiCatalogMapper extends MyBaseMapper<ApiCatalogPo> {
    /**
     *  根据主键删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(String id);

    /**
     * 插入
     * @param apiCatalogVo
     * @return
     */
    int insert(ApiCatalogVo apiCatalogVo);

    /**
     * 分页查询
     * @param apiCatalogVo
     * @param page
     * @return
     */
    List<ApiCatalogVo> select(Page page, @Param("apiCatalogVo") ApiCatalogVo apiCatalogVo);

    /**
     * 不分页查询
     * @param apiCatalogVo
     * @return
     */
    List<ApiCatalogVo> selectAll(@Param("apiCatalogVo")ApiCatalogVo apiCatalogVo);

    /**
     * 查询目录树结构
     * @param apiCatalogVo
     * @return
     */
    List<ApiCatalogVo> selectCatalogTree(@Param("apiCatalogVo")ApiCatalogVo apiCatalogVo);

    /**
     * 子目录查询
     * @param apiCatalogVo
     * @return
     */
    List<ApiCatalogVo> selectChild(ApiCatalogVo apiCatalogVo);
    /**
     * 根据索引查询
     * @param apiCatalogVo
     * @return
     */
    List<ApiCatalogVo> selectByIndex( @Param("apiCatalogVo") ApiCatalogVo apiCatalogVo);


    /**
     * 根据目录名称查询对应的api
     * @param apiVo
     * @param page
     * @return
     */
    List<ApiVo> selectApiByCatalog(Page page,@Param("apiVo") ApiVo apiVo);

    /**
     * 根据目录查询关联的api数量
     * @param apiVo
     * @return
     */
    Integer selectApiCountByCatalog(ApiVo apiVo);

    /**
     * 根据主键更新
     * @param apiCatalogVo
     * @return
     */
    int updateByPrimaryKey(ApiCatalogVo apiCatalogVo);
}