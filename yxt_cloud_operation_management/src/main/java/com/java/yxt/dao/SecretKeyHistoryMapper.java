package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.SecretKeyHistoryVo;
import com.java.yxt.vo.SecretKeyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 密钥记录管理表dao
 * @author 蔡家明
 */
@Mapper
public interface SecretKeyHistoryMapper {
    /**
     * 分页查询
     * @param secretKeyVo
     * @param page
     * @return
     */
    List<SecretKeyHistoryVo> select(Page page, @Param("secretKeyVo") SecretKeyVo secretKeyVo);

    /**
     * 不分页查询
     * @param secretKeyVo
     * @return
     */
    List<SecretKeyHistoryVo> selectAll(@Param("secretKeyVo") SecretKeyVo secretKeyVo);

    /**
     * 插入
     * @param secretKeyHistoryVo
     * @return
     */
    int insert(SecretKeyHistoryVo secretKeyHistoryVo);

    /**
     * 修改
     * @param secretKeyHistoryVo
     * @return
     */
    int update(SecretKeyHistoryVo secretKeyHistoryVo);

}
