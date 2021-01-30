package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.dao.TerminalGroupMidMapper;
import com.java.yxt.dao.TerminalGroupMapper;
import com.java.yxt.dao.TerminalMapper;
import com.java.yxt.po.TerminalGroupPo;
import com.java.yxt.po.TerminalPo;
import com.java.yxt.service.ITerminalGroupService;
import com.java.yxt.service.TerminalService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.vo.TerminalGroupMidPo;
import com.java.yxt.vo.TerminalGroupVo;
import com.java.yxt.vo.TerminalVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang Liang
 * @version 1.0.0
 * @time 2021-01-19 10:51
 */

@Slf4j
@Service
public class TerminalGroupServiceImpl implements ITerminalGroupService {

    @Autowired
    TerminalGroupMapper terminalGroupMapper;

    @Autowired
    TerminalGroupMidMapper terminalGroupMidMapper;

    @Autowired
    TerminalMapper terminalMapper;

    @Override
    public Response addTerminalGroup(TerminalGroupVo terminalGroupVo) {
        try {
            log.info("新增终端分组-开始：{}",terminalGroupVo);
            TerminalGroupPo addGroupInfoPo = new TerminalGroupPo();
            BeanUtils.copyProperties(terminalGroupVo, addGroupInfoPo);
            addGroupInfoPo.setValidStatus(0);
            terminalGroupMapper.addTerminalGroup(terminalGroupVo);
            log.info("新增终端分组-新增分组完毕：{}",terminalGroupVo);
            List<TerminalGroupMidPo> midPoList = new ArrayList<>();
            for (String terminalId : terminalGroupVo.getTerminalIdList()){
                TerminalGroupMidPo terminalGroupMidPo = new TerminalGroupMidPo();
                terminalGroupMidPo.setGroupId(terminalGroupVo.getId());
                terminalGroupMidPo.setTerminalId(terminalId);
                terminalGroupMidPo.setCreaterId(terminalGroupVo.getCreaterId());
                terminalGroupMidPo.setCreateTime(DateTimeUtil.getDate());
                terminalGroupMidPo.setCreaterName(terminalGroupVo.getCreaterName());
                terminalGroupMidPo.setOrgId(terminalGroupVo.getOrgId());
                midPoList.add(terminalGroupMidPo);
            }
            terminalGroupMidMapper.addGroupMid(midPoList);
            log.info("新增终端分组-新增映射完毕,数量：【{}】",midPoList.size());
            return Response.success();
        } catch (Exception e) {
            log.error("新增终端分组异常！",e);
            return new Response(ValidationEnum.MGT_GROUP_TERMINAL_EXCEPTION);
        }
    }

    @Override
    public Response queryTerminalGroupInfo(TerminalGroupVo terminalGroupVo) {
        try {
            log.info("分页查询终端分组列表-开始，{}",terminalGroupVo);
            TerminalGroupPo terminalGroupPo = new TerminalGroupPo();
            BeanUtils.copyProperties(terminalGroupVo, terminalGroupPo);
            terminalGroupPo.setCustomerId(terminalGroupVo.getOrgId());
            Page<List<TerminalGroupPo>> page = new Page<>(terminalGroupVo.getCurrent(), terminalGroupVo.getSize());
            IPage<List<TerminalGroupPo>> iPage = terminalGroupMapper.queryTerminalGroupInfo(page, terminalGroupPo);
            log.info("分页查询终端分组列表-返回：{}", iPage.getRecords());
            return Response.successWithResult(iPage);
        } catch (BeansException e) {
            log.error("分页查询终端分组列表-异常！",e);
            return new Response(ValidationEnum.MGT_GROUP_TERMINAL_EXCEPTION);
        }
    }

    @Override
    public Response queryTerminalGroupDetail(TerminalGroupVo terminalGroupVo) {
        try {
            log.info("分页查询终端详情列表-开始，{}",terminalGroupVo);
            TerminalVo queryTerminal = new TerminalVo();
            queryTerminal.setMsisdn(terminalGroupVo.getMsisdn());
            queryTerminal.setTerminalFactory(terminalGroupVo.getTerminalFactory());
            queryTerminal.setCustomerId(terminalGroupVo.getOrgId());
            queryTerminal.setType(terminalGroupVo.getTerminalType());
            Page<List<TerminalVo>> page = new Page<>(queryTerminal.getCurrent(), queryTerminal.getSize());
            IPage<List<TerminalVo>> iPage = terminalMapper.queryTerminalInfo(page,queryTerminal);
            return Response.successWithResult(iPage);
        } catch (Exception e) {
            log.error("分页查询终端详情列表-异常！",e);
            return new Response(ValidationEnum.MGT_GROUP_TERMINAL_EXCEPTION);
        }
    }

    @Override
    public Response updateValidStatus(TerminalGroupVo terminalGroupVo) {
        try {
            log.info("修改终端分组状态-开始,{}",terminalGroupVo);
            TerminalGroupPo terminalGroupPo = new TerminalGroupPo();
            BeanUtils.copyProperties(terminalGroupVo, terminalGroupPo);
            terminalGroupPo.setUpdateTime(DateTimeUtil.getDate());
            terminalGroupPo.setCustomerId(terminalGroupVo.getOrgId());
            int num = terminalGroupMapper.updateValidStatus(terminalGroupVo);
            log.info("修改终端分组状态-成功,修改数量{}",num);
            return Response.success();
        } catch (Exception e) {
            log.error("修改终端分组状态-异常！",e);
            return new Response(ValidationEnum.MGT_GROUP_TERMINAL_EXCEPTION);
        }
    }

    @Override
    public Response deleteTerminalGroup(TerminalGroupVo terminalGroupVo) {
        try {
            log.info("删除终端分组-开始,{}",terminalGroupVo);
            TerminalGroupMidPo deleteMidPo = new TerminalGroupMidPo();
            deleteMidPo.setGroupId(terminalGroupVo.getId());
            deleteMidPo.setCreaterId(terminalGroupVo.getCreaterId());
            deleteMidPo.setOrgId(terminalGroupVo.getOrgId());
            int delSize = terminalGroupMidMapper.deleteRelationship(deleteMidPo);
            log.info("删除原关联关系,数量：{}",delSize);
            TerminalGroupPo editGroupInfoPo = new TerminalGroupPo();
            BeanUtils.copyProperties(terminalGroupVo, editGroupInfoPo);
            terminalGroupMapper.deleteTerminalGroup(editGroupInfoPo);
            log.info("删除终端分组-结束：{}",delSize);
            return Response.success();
        } catch (BeansException e) {
            log.error("删除终端分组-异常！",e);
            return new Response(ValidationEnum.MGT_GROUP_TERMINAL_EXCEPTION);
        }
    }

    @Override
    public Response editTerminalGroupInfo(TerminalGroupVo terminalGroupVo) {
        try {
            log.info("编辑分组信息及关联终端信息-开始,{}",terminalGroupVo);
            TerminalGroupMidPo deleteMidPo = new TerminalGroupMidPo();
            deleteMidPo.setGroupId(terminalGroupVo.getId());
            deleteMidPo.setCreaterId(terminalGroupVo.getCreaterId());
            deleteMidPo.setOrgId(terminalGroupVo.getOrgId());
            int delSize = terminalGroupMidMapper.deleteRelationship(deleteMidPo);
            log.info("删除原关联关系,数量：{}",delSize);
            List<TerminalGroupMidPo> midPoList = new ArrayList<>();
            for (String terminalId : terminalGroupVo.getTerminalIdList()){
                TerminalGroupMidPo terminalGroupMidPo = new TerminalGroupMidPo();
                terminalGroupMidPo.setGroupId(terminalGroupVo.getId());
                terminalGroupMidPo.setTerminalId(terminalId);
                terminalGroupMidPo.setCreaterId(terminalGroupVo.getCreaterId());
                terminalGroupMidPo.setCreateTime(DateTimeUtil.getDate());
                terminalGroupMidPo.setCreaterName(terminalGroupVo.getCreaterName());
                terminalGroupMidPo.setOrgId(terminalGroupVo.getOrgId());
                midPoList.add(terminalGroupMidPo);
            }
            terminalGroupMidMapper.addGroupMid(midPoList);
            log.info("编辑分组信息及关联终端信息-新增映射完毕,数量：【{}】",midPoList.size());
            TerminalGroupPo editGroupInfoPo = new TerminalGroupPo();
            BeanUtils.copyProperties(terminalGroupVo, editGroupInfoPo);
            editGroupInfoPo.setUpdateTime(DateTimeUtil.getDate());
            editGroupInfoPo.setCustomerId(terminalGroupVo.getOrgId());
            terminalGroupMapper.editTerminalGroupInfo(editGroupInfoPo);
            log.info("编辑分组信息及关联终端信息-更新分组基础信息结束：{}",editGroupInfoPo);
            return Response.success();
        } catch (BeansException e) {
            log.error("删除终端分组-异常！",e);
            return new Response(ValidationEnum.MGT_GROUP_TERMINAL_EXCEPTION);
        }
    }
}
