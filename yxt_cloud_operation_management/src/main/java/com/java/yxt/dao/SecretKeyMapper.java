package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.SecretKeyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 密钥管理表dao
 * @author 蔡家明
 */
@Mapper
public interface SecretKeyMapper {
    /**
     * 分页查询
     * @param secretKeyVo
     * @param page
     * @return
     */
    List<SecretKeyVo> select(Page page, @Param("secretKeyVo") SecretKeyVo secretKeyVo);

    /**
     * 不分页查询
     * @param secretKeyVo
     * @return
     */
    List<SecretKeyVo> selectAll(@Param("secretKeyVo") SecretKeyVo secretKeyVo);

    /**
     * 插入
     * @param secretKeyVo
     * @return
     */
    int insert(SecretKeyVo secretKeyVo);

    /**
     * 更新
     * @param secretKeyVo
     * @return
     */
    int update(SecretKeyVo secretKeyVo);


    /**
     * 删除
     * @param secretKeyVo
     * @return
     */
    int delete(SecretKeyVo secretKeyVo);

    /**
     * 查询客户密钥
     * @param secretKeyVo
     * @return
     */
    SecretKeyVo selectByCustomerCode(@Param("secretKeyVo") SecretKeyVo secretKeyVo);

    /**
     * 查询客户有效密钥
     * @param secretKeyVo
     * @return
     */
    List<SecretKeyVo> selectValidByCustomerCode(@Param("secretKeyVo") SecretKeyVo secretKeyVo);

}
