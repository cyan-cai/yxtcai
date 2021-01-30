package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.IpWhiteVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * ip白名单接口
 * @author  zanglei
 */
public interface IpWhiteService {
    /**
     * 插入
     * @param ipWhiteVo
     * @return
     */
    int insert(IpWhiteVo ipWhiteVo);

    /**
     * 更新
     * @param ipWhiteVo
     * @return
     */
    int update(IpWhiteVo ipWhiteVo);

    /**
     * 分页查询
     * @param page
     * @param ipWhiteVo
     * @return
     */
    List<IpWhiteVo> select(Page page, IpWhiteVo ipWhiteVo);

    /**
     * 不分页查询
     * @param ipWhiteVo
     * @return
     */
    List<IpWhiteVo> selectAll(IpWhiteVo ipWhiteVo);

    /**
     * 文件导出
     * @param ipWhiteVo
     * @param response
     */
    void export(IpWhiteVo ipWhiteVo, HttpServletResponse response);
}
