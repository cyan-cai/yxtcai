package com.java.yxt.controller;

import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.po.TerminalGroupPo;
import com.java.yxt.service.ITerminalGroupService;
import com.java.yxt.vo.TerminalGroupVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jiang Liang
 * @version 1.0.0
 * @time 2021-01-18 14:05
 */
@RestController
@Slf4j
@Api(tags = {"终端分组管理"})
@RequestMapping("/terminalGroup")
public class TerminalGroupController {

    @Autowired
    ITerminalGroupService iTerminalGroupService;

    @RequestMapping(value = "/addTerminalGroup",method = RequestMethod.POST)
    @SetCreaterName
    @ApiOperation(value = "新增终端分组",httpMethod = "POST")
    @SOLog(tableName = "mgt_group_terminal",remark = "新增终端分组" ,type = OperationType.post)
    public Response addTerminalGroup(@RequestBody TerminalGroupVo terminalGroupVo){
        return iTerminalGroupService.addTerminalGroup( terminalGroupVo);
    }

    @RequestMapping(value = "/queryTerminalGroupInfo",method = RequestMethod.POST)
    @SetCreaterName
    @ApiOperation(value = "查询分组分页列表信息",httpMethod = "GET")
    public Response queryTerminalGroupInfo(@RequestBody TerminalGroupVo terminalGroupVo){
        return iTerminalGroupService.queryTerminalGroupInfo( terminalGroupVo);
    }

    @RequestMapping(value = "/queryTerminalGroupDetail",method = RequestMethod.POST)
    @SetCreaterName
    @ApiOperation(value = "查询分组终端详情",httpMethod = "GET")
    public Response queryTerminalGroupDetail(@RequestBody TerminalGroupVo terminalGroupVo){
        return iTerminalGroupService.queryTerminalGroupDetail( terminalGroupVo);
    }

    @RequestMapping(value = "/updateValidStatus",method = RequestMethod.POST)
    @SetCreaterName
    @ApiOperation(value = "变更分组有效标识",httpMethod = "POST")
    @SOLog(tableName = "mgt_group_terminal",remark = "新增终端分组" ,type = OperationType.post)
    public Response updateValidStatus(@RequestBody TerminalGroupVo terminalGroupVo){
        return iTerminalGroupService.updateValidStatus( terminalGroupVo);
    }

    @RequestMapping(value = "/deleteTerminalGroup",method = RequestMethod.POST)
    @SetCreaterName
    @ApiOperation(value = "删除分组",httpMethod = "POST")
    @SOLog(tableName = "mgt_group_terminal",remark = "新增终端分组" ,type = OperationType.post)
    public Response deleteTerminalGroup(@RequestBody TerminalGroupVo terminalGroupVo){
        return iTerminalGroupService.deleteTerminalGroup( terminalGroupVo);
    }

    @RequestMapping(value = "/editTerminalGroupInfo",method = RequestMethod.POST)
    @SetCreaterName
    @ApiOperation(value = "编辑分组信息",httpMethod = "POST")
    @SOLog(tableName = "mgt_group_terminal",remark = "新增终端分组" ,type = OperationType.post)
    public Response editTerminalGroupInfo(@RequestBody TerminalGroupVo terminalGroupVo){
        return iTerminalGroupService.editTerminalGroupInfo( terminalGroupVo);
    }



}
