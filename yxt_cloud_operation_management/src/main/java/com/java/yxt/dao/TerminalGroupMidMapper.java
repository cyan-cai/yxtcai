package com.java.yxt.dao;

import com.java.yxt.vo.TerminalGroupMidPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Jiang Liang
 * @version 1.0.0
 * @time 2021-01-19 11:14
 */
@Mapper
public interface TerminalGroupMidMapper {

    void addGroupMid(List<TerminalGroupMidPo> terminalGroupVo);

    int deleteRelationship(TerminalGroupMidPo terminalGroupMidPo);
}
