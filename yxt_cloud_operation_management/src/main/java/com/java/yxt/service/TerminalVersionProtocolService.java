package com.java.yxt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.TerminalVersionProtocolVo;

import java.util.List;

/**
 * 终端版本协议管理接口
 */
public interface TerminalVersionProtocolService {
    /**
     * 添加记录
     * @param terminalVersionProtocolVo
     * @return
     */
    int addRecord(TerminalVersionProtocolVo terminalVersionProtocolVo);

    /**
     * 更新记录
     * @param terminalVersionProtocolVo
     * @return
     */
    int update(TerminalVersionProtocolVo terminalVersionProtocolVo);

    /**
     * 删除
     * @param terminalVersionProtocolVo
     * @param terminalVersionProtocolVo
     * @return
     */
    int delete(TerminalVersionProtocolVo terminalVersionProtocolVo);

    /**
     * 分页查询
     * @param page
     * @param terminalVersionProtocolVo
     * @return
     */
    IPage<TerminalVersionProtocolVo> selectByPage(Page page, TerminalVersionProtocolVo terminalVersionProtocolVo);

    /**
     * 不分页查询
     * @param terminalVersionProtocolVo
     * @return
     */
    List<TerminalVersionProtocolVo> selectAll(TerminalVersionProtocolVo terminalVersionProtocolVo);

}
