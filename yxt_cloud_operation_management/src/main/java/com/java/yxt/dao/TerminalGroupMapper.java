package com.java.yxt.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.po.ServicePo;
import com.java.yxt.po.TerminalGroupPo;
import com.java.yxt.vo.TerminalGroupVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Jiang Liang
 * @version 1.0.0
 * @time 2021-01-19 11:14
 */
@Mapper
public interface TerminalGroupMapper{


    void addTerminalGroup(TerminalGroupVo terminalGroupVo);

    IPage<List<TerminalGroupPo>> queryTerminalGroupInfo(Page<List<TerminalGroupPo>> page, TerminalGroupPo terminalGroupPo);

    int updateValidStatus(TerminalGroupVo terminalGroupVo);

    void editTerminalGroupInfo(TerminalGroupPo editGroupInfoPo);

    void deleteTerminalGroup(TerminalGroupPo editGroupInfoPo);

}
