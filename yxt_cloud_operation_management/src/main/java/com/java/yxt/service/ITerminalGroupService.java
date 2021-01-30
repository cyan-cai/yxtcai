package com.java.yxt.service;

import com.java.yxt.base.Response;
import com.java.yxt.po.TerminalGroupPo;
import com.java.yxt.vo.TerminalGroupVo;
import org.springframework.stereotype.Service;

/**
 * @author Jiang Liang
 * @version 1.0.0
 * @time 2021-01-19 10:45
 */
public interface ITerminalGroupService {

    Response addTerminalGroup(TerminalGroupVo terminalGroupVo);

    Response queryTerminalGroupInfo(TerminalGroupVo terminalGroupVo);

    Response queryTerminalGroupDetail(TerminalGroupVo terminalGroupVo);

    Response updateValidStatus(TerminalGroupVo terminalGroupVo);

    Response deleteTerminalGroup(TerminalGroupVo terminalGroupVo);

    Response editTerminalGroupInfo(TerminalGroupVo terminalGroupVo);
}
