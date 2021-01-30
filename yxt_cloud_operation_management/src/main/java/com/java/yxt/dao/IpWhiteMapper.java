package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.po.IpWhitePo;
import com.java.yxt.vo.IpWhiteVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 终端用户白名单
 * @author zanglei
 */
@Mapper
public interface IpWhiteMapper extends MyBaseMapper<IpWhitePo>{

    /**
     * 插入
     * @param ipWhiteVo
     * @return
     */
    int insert(IpWhiteVo ipWhiteVo);

    /**
     * 分页查询
     * @param page
     * @param ipWhiteVo
     * @return
     */
    List<IpWhiteVo> select(Page page, @Param("ipWhiteVo") IpWhiteVo ipWhiteVo);

    /**
     * 不分页查询
     * @param ipWhiteVo
     * @return
     */
    List<IpWhiteVo> selectAll(@Param("ipWhiteVo") IpWhiteVo ipWhiteVo);

    /**
     * 更新
     * @param ipWhiteVo
     * @return
     */
    int update(IpWhiteVo ipWhiteVo);

    /**
     * 根据条件查询
     * @param ipWhiteVo
     * @return
     */
    List<IpWhiteVo> selectByContion(IpWhiteVo ipWhiteVo);
}