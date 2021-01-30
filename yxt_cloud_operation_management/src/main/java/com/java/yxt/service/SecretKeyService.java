package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.SecretKeyHistoryVo;
import com.java.yxt.vo.SecretKeyVo;

import java.util.List;

/**
 * 密钥管理
 * @author 蔡家明
 */
public interface SecretKeyService {

    /**
     * 分页查询
     * @param secretKeyVo
     * @param page
     * @return
     */
    List<SecretKeyVo> select(Page page, SecretKeyVo secretKeyVo);

    /**
     * 不分页查询
     * @param secretKeyVo
     * @return
     */
    List<SecretKeyVo> selectAll(SecretKeyVo  secretKeyVo);


    /**
     * 新增
     * @param secretKeyVo
     * @return
     */
    int insert(SecretKeyVo  secretKeyVo);

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
     * 启用/禁用密钥
     * @param secretKeyVo
     * @return
     */
    int enableAndDisable(SecretKeyVo secretKeyVo);

    /**
     * 启用
     * @param secretKeyVo
     * @return
     */
    int enable(SecretKeyVo secretKeyVo);

    /**
     * 禁用
     * @param secretKeyVo
     * @return
     */
    int disable(SecretKeyVo secretKeyVo);

    /**
     * 分页查询密钥历史记录
     * @param secretKeyVo
     * @param page
     * @return
     */
    List<SecretKeyHistoryVo> selectKeyHistory(Page page, SecretKeyVo secretKeyVo);

    /**
     * 不分页查询密钥历史记录
     * @param secretKeyVo
     * @return
     */
    List<SecretKeyHistoryVo> selectAllKeyHistory(SecretKeyVo  secretKeyVo);
}
