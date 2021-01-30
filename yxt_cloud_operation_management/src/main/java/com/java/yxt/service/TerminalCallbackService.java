package com.java.yxt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.vo.TerminalCallbackVo;

import java.util.List;

/**
 * 回调地址查询关系表
 * @author zanglei
 */
public interface TerminalCallbackService {
    /**
     * 插入
     * @param terminalCallbackServiceVo
     * @return
     */
    int insert(TerminalCallbackVo terminalCallbackServiceVo);

    /**
     * 修改
     * @param terminalCallbackVo
     * @return
     */
    int update(TerminalCallbackVo terminalCallbackVo);


    /**
     * 修改
     * @param terminalCallbackVo
     * @return
     */
    int updateByServiceAndTerminalId(TerminalCallbackVo terminalCallbackVo);

    /**
     * 查询
     * @param terminalCallbackServiceVo
     * @param page
     * @return
     */
    List<TerminalCallbackVo> select(Page page, TerminalCallbackVo terminalCallbackServiceVo);
}
