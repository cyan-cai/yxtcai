package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.po.ApiPo;
import com.java.yxt.vo.ApiVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * api dao 层接口
 * @author zanglei
 */
@Mapper
public interface ApiMapper extends MyBaseMapper<ApiPo>{
    /**
     * 删除
     * @param id
     * @return
     */
    @SOLog(type = OperationType.delete,tableName = "mgt_api",remark = "删除api",mapperName = "apiMapper")
    int delete(@Param("id") Long id);

    /**
     * 插入
     * @param apiVo
     * @return
     */
    int insert(ApiVo apiVo);

    /**
     * 分页查询
     * @param apiVo
     * @param page
     * @return
     */
    List<ApiVo> select(Page<?> page, @Param("apiVo") ApiVo apiVo);

    /**
     * 查询
     * @param apiVo
     * @return
     */
    List<ApiVo> selectAll(ApiVo apiVo);


    /**
     * 未被销售品管理的api查询
     * @param apiVo
     * @return
     */
    List<ApiVo> selectAllUnRelation(ApiVo apiVo);

    /**
     * 更新
     * @param apiVo
     * @return
     */
    int update(ApiVo apiVo);

    /**
     * 根据条件查询
     * @param apiVo
     * @return
     */
    ApiVo selectByContion(@Param("apiVo") ApiVo apiVo);


    /**
     * 未下挂api查询
     * @param apiVo
     * @return
     */
    List<ApiVo> unAttachApiSelect(Page page,@Param("apiVo") ApiVo apiVo);

    /**
     * 已下挂api查询
     * @param apiVo
     * @return
     */
    List<ApiVo> attachedApiSelect(Page page,@Param("apiVo") ApiVo apiVo);

    /**
     * 根据销售品ID查询API
     * @param productId
     * @return
     */
    List<ApiVo> selectBySaleCode(String productId);
}