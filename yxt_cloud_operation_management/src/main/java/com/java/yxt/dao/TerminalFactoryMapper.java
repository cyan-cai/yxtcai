package com.java.yxt.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.TerminalFactoryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 终端厂商管理表dao
 * @author 蔡家明
 */
@Mapper
public interface TerminalFactoryMapper {
    /**
     * 分页查询
     * @param terminalFactoryVo
     * @param page
     * @return
     */
    List<TerminalFactoryVo> select(Page page, @Param("terminalFactoryVo") TerminalFactoryVo terminalFactoryVo);

    /**
     * 不分页查询
     * @param terminalFactoryVo
     * @return
     */
    List<TerminalFactoryVo> selectAll(@Param("terminalFactoryVo")TerminalFactoryVo terminalFactoryVo);

    /**
     * 插入
     * @param terminalFactoryVo
     * @return
     */
    int insert(TerminalFactoryVo terminalFactoryVo);

    /**
     * 更新
     * @param terminalFactoryVo
     * @return
     */
    int update(TerminalFactoryVo terminalFactoryVo);

    /**
     * 删除
     * @param terminalFactoryVo
     * @return
     */
    int delete(TerminalFactoryVo terminalFactoryVo);

    /**
     * 根据厂商名称查询厂商
     * @param terminalFactoryVo
     * @return
     */
    TerminalFactoryVo selectByFactoryName(TerminalFactoryVo terminalFactoryVo);

    /**
     * 模糊查询状态正常的厂商
     * @param terminalFactoryVo
     * @return
     */
    List<TerminalFactoryVo> selectFactoryName(TerminalFactoryVo terminalFactoryVo);

    /**
     * 根据厂商ID查询厂商
     * @param id
     * @return
     */
    TerminalFactoryVo selectById(String id);

}
