package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.po.TerminalWhitePo;
import com.java.yxt.vo.TerminalWhiteVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 终端白名单dao
 * @author zanglei
 */
@Mapper
public interface TerminalWhiteMapper extends MyBaseMapper<TerminalWhitePo>{
    /**
     * 删除
     * @param terminalWhiteVo
     * @return
     */
    int delete(TerminalWhiteVo terminalWhiteVo);

    /**
     * 插入
     * @param terminalWhiteVo
     * @return
     */
    @SetCreaterName
    int insert(TerminalWhiteVo terminalWhiteVo);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertList(List<TerminalWhiteVo> list);

    /**
     * 判断此终端对应的业务标识是否存在
     * @param terminalWhiteVo
     * @return
     */
    List<TerminalWhiteVo> isTerminalServiceCodeExists(TerminalWhiteVo terminalWhiteVo);

  /**
     * 分页查询
     * @param page
     * @param terminalWhiteVo
     * @return
     */
    List<TerminalWhiteVo> select(Page<?> page, @Param("terminalWhiteVo") TerminalWhiteVo terminalWhiteVo);

    /**
     * 不分页
     * @param terminalWhiteVo
     * @return
     */
    List<TerminalWhiteVo> selectAll( @Param("terminalWhiteVo") TerminalWhiteVo terminalWhiteVo);

    /**
     * 查询白名单中终端信息
     * @param terminalWhiteVo
     * @return
     */
    List<TerminalWhiteVo> terminalWhiteInfo( @Param("terminalWhiteVo") TerminalWhiteVo terminalWhiteVo);

    /**
     * 更新
     * @param terminalWhiteVo
     * @return
     */
    int update(TerminalWhiteVo terminalWhiteVo);
}