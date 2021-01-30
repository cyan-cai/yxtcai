package com.java.yxt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.TerminalFactoryService;
import com.java.yxt.vo.TerminalFactoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TerminalFactoryController
 *
 * @author 蔡家明
 * @version V1.0
 * @description 终端厂商管理
 * @Package com.java.yxt.controller
 * @date 2020/12/09
 */
@RestController
@Slf4j
@Api(tags = {"终端厂商管理"})
@RequestMapping("terminalFactory")
public class  TerminalFactoryController {
    @Autowired
    TerminalFactoryService terminalFactoryService;

    @PostMapping("/add")
    @ApiOperation("新增终端厂商")
    @SOLog(tableName = "mgt_terminal_factory", remark = "新增终端厂商" ,type = OperationType.post)
    public Response addTerminalFactory(@RequestBody TerminalFactoryVo terminalFactoryVo) {
        terminalFactoryService.insert(terminalFactoryVo);
        return Response.successWithResult(terminalFactoryVo.getId());
    }

    @PostMapping("/update")
    @ApiOperation("更新终端厂商")
    @SOLog(tableName = "mgt_terminal_factory", remark = "更新终端厂商" ,type = OperationType.put)
    public Response updateTerminalFactory(@RequestBody TerminalFactoryVo terminalFactoryVo) {
        terminalFactoryService.update(terminalFactoryVo);
        return Response.success();
    }

    @PostMapping("/enableOrDisable")
    @ApiOperation("注销/启用终端厂商")
    @SOLog(tableName = "mgt_terminal_factory", remark = "注销/启用终端厂商" ,type = OperationType.put)
    public Response enableOrDisableTerminalFactory(@RequestBody TerminalFactoryVo terminalFactoryVo) {
        terminalFactoryService.enableOrDisable(terminalFactoryVo);
        return Response.success();
    }

    @PostMapping("/delete")
    @ApiOperation("删除终端厂商")
    public Response deleteTerminalFactory(@RequestBody TerminalFactoryVo terminalFactoryVo) {
        terminalFactoryService.delete(terminalFactoryVo);
        return Response.success();
    }

    @PostMapping("/select")
    @ApiOperation("分页查询终端厂商")
    public Response selectTerminalFactory(@RequestBody TerminalFactoryVo terminalFactoryVo) {
        Page<TerminalFactoryVo> page = new Page<>(terminalFactoryVo.getCurrent(),terminalFactoryVo.getSize());
        page.setRecords(terminalFactoryService.select(page,terminalFactoryVo));
        return Response.successWithResult(page);
    }

    @PostMapping("/selectAll")
    @ApiOperation("不分页查询终端厂商")
    public Response selectAllTerminalDevice(@RequestBody TerminalFactoryVo terminalFactoryVo) {
        List<TerminalFactoryVo> list=terminalFactoryService.selectAll(terminalFactoryVo);
        return Response.successWithResult(list);
    }

    @PostMapping("/selectFactoryName")
    @ApiOperation("模糊查询状态正常的终端厂商")
    public Response selectFactoryName(@RequestBody TerminalFactoryVo terminalFactoryVo) {
        List<TerminalFactoryVo> list=terminalFactoryService.selectFactoryName(terminalFactoryVo);
        return Response.successWithResult(list);
    }


}
