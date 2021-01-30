package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.TerminalFactoryVo;

import java.util.List;

/**
 * 终端厂商管理
 * @author 蔡家明
 */
public interface TerminalFactoryService {

    /**
     * 分页查询
     * @param terminalFactoryVo
     * @param page
     * @return
     */
    List<TerminalFactoryVo> select(Page page, TerminalFactoryVo terminalFactoryVo);

    /**
     * 不分页查询
     * @param terminalFactoryVo
     * @return
     */
    List<TerminalFactoryVo> selectAll( TerminalFactoryVo terminalFactoryVo);

    /**
     * 模糊查询状态正常的厂商
     * @param terminalFactoryVo
     * @return
     */
    List<TerminalFactoryVo> selectFactoryName( TerminalFactoryVo terminalFactoryVo);

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
     * 注销
     * @param terminalFactoryVo
     * @return
     */
    int enableOrDisable(TerminalFactoryVo terminalFactoryVo);
}
