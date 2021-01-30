package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.dao.ServeridMapper;
import com.java.yxt.service.ServeridService;
import com.java.yxt.vo.ServeridVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * Serverid Service implement
 * </p>
 *
 * @author zanglei
 * @date 2020-09-25
 */
@Service
public class ServeridServiceImpl implements ServeridService {

    @Autowired
    private ServeridMapper serveridMapper;

    /**
     * <p>
     * Select Serverid by id
     * </p>
     * @param serveridVo
     */
    @Override
    public List<ServeridVo> select(Page page, ServeridVo serveridVo) {
        return serveridMapper.select(page,serveridVo);
    }

    /**
     * <p>
     * Select List<Serverid>
     * </p>
     * @param serveridVo
     */
    @Override
    public List<ServeridVo> list(ServeridVo serveridVo) {
        return serveridMapper.selectList(serveridVo);
    }

    /**
     * <p>
     * Add Serverid
     * </p>
     * @param serveridVo
     */
    @Override
    public int save(ServeridVo serveridVo) {
       return serveridMapper.save(serveridVo);
    }

    /**
     * <p>
     * Update Serverid
     * </p>
     * @param serveridVo
     */
    @Override
    public int updateById(ServeridVo serveridVo) {
       return serveridMapper.updateById(serveridVo);
    }

    /**
     * <p>
     * Delete Serverid by id
     * </p>
     * @param serveridVo
     */
    @Override
    public int removeById(ServeridVo serveridVo) {
       return serveridMapper.removeById(serveridVo);
    }
}
