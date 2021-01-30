package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.ServeridVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * serverId管理表 Mapper Interface
 * </p>
 *
 * @author zanglei
 * @date 2020-09-25
 */
@Mapper
public interface ServeridMapper {

    /**
     * select Serverid by id
     *
     * @param serveridVo
     * @param page
     * @return List<ServeridPo>
     */
    List<ServeridVo> select(Page page, @Param("serveridVo") ServeridVo serveridVo);

    /**
     * select Serverid List
     *
     * @param  serveridVo
     * @return List<ServeridPo>
     */
    List<ServeridVo> selectList(ServeridVo serveridVo);

    /**
     * insert Serverid
     *
     * @param serveridVo
     * @return int
     */
    int save(ServeridVo serveridVo);

    /**
     * update Serverid
     *
     * @param serveridVo
     * @return int
     */
    int updateById(ServeridVo serveridVo);

    /**
     * 删除
     * @param serveridVo
     * @return
     */
    int removeById(ServeridVo serveridVo);
}

