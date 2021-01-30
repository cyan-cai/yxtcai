package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.ServeridVo;

import java.util.List;

/**
 * <p>
 * Serverid Service
 * </p>
 *
 * @author zanglei
 * @date 2020-09-25
 */
public interface ServeridService {

    /**
     * <p>
     * Find Serverid by id
     * </p>
     * @param serveridVo
     * @param page
     * @return List<ServeridVo>
     */
     List<ServeridVo> select(Page page, ServeridVo serveridVo);

    /**
     * <p>
     * Select List<Serverid>
     * </p>
     * @param serveridVo
     * @return List<ServeridVo>
     */
     List<ServeridVo> list(ServeridVo serveridVo);

    /**
     * <p>
     * Add Serverid
     * </p>
     * @param  serveridVo
     * @return int
     */
    int  save(ServeridVo serveridVo);

    /**
     * <p>
     * Update Serverid
     * </p>
     * @param serveridVo
     * @return int
     */
     int updateById(ServeridVo serveridVo);

    /**
     * <p>
     * Delete Serverid by id
     * </p>
     * @param serveridVo
     * @return int
     */
    int removeById(ServeridVo serveridVo);
}
