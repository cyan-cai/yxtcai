package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.ApiVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * api管理模块
 * @author  zanglei
 */
public interface Apiservice {
     /**
      * 插入
      * @param apiVo
      * @return
      */
     int insert(ApiVo apiVo);

     /**
      * 更新
      * @param apiVo
      * @return
      */
     int update(ApiVo apiVo);

     /**
      * 查询（分页）
      * @param apiVo
      * @param page
      * @return
      */
     List<ApiVo> select(Page<?> page, @Param("apiVo") ApiVo apiVo);


     /**
      * 查询（不分页）
      * @param apiVo
      * @return
      */
     List<ApiVo> selectAll(ApiVo apiVo);


     /**
      * 查询（不分页）
      * @param apiVo
      * @return
      */
     List<ApiVo> selectAllUnRelation(ApiVo apiVo);

     /**
      * 删除
      * @param apiVo
      * @return
      */
     int delete(ApiVo apiVo);

     /**
      * 未下挂api查询
      * @param apiVo
      * @return
      */
     List<ApiVo> unAttachApiSelect(Page page,ApiVo apiVo);

     /**
      * 已下挂api查询
      * @param apiVo
      * @return
      */
     List<ApiVo> attachedApiSelect(Page page,ApiVo apiVo);
}
