package com.java.yxt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.TerminalCallbackService;
import com.java.yxt.vo.TerminalCallbackVo;
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
 * TerminalCallbackController
 *
 * @author zanglei
 * @version V1.0
 * @description 回调关系处理
 * @Package com.java.yxt.controller
 * @date 2020/9/17
 */
@RestController
@Slf4j
@RequestMapping("terminalcallback")
@Api(tags = {"回调关系管理"})
public class TerminalCallbackController {

    @Autowired
    TerminalCallbackService terminalCallbackService;

    @PostMapping("/add")
    @ApiOperation("添加回调关系数据")
    @SetCreaterName
    @SOLog(tableName = "mgt_terminal_callback_service",remark = "添加回调关系" ,type = OperationType.post)
    public Response add(@RequestBody TerminalCallbackVo terminalCallbackVo){
        terminalCallbackService.insert(terminalCallbackVo);
        return Response.successWithResult(terminalCallbackVo.getId());
    }

    @PostMapping("/update")
    @ApiOperation("更新回调关系")
    @SOLog(type = OperationType.put,tableName = "mgt_terminal_callback_service",remark = "更新回调关系",
            mapperName = "terminalCallbackMapper",paramKey = "id")
    public Response update(@RequestBody TerminalCallbackVo terminalCallbackVo){
        terminalCallbackService.update(terminalCallbackVo);
        return Response.success();
    }

    @PostMapping("/select")
    @ApiOperation("查询回调关系")
    public Response select(@RequestBody TerminalCallbackVo terminalCallbackVo){
        Page<TerminalCallbackVo> page = new Page<>(terminalCallbackVo.getCurrent(),terminalCallbackVo.getSize());
        List<TerminalCallbackVo> list=   terminalCallbackService.select(page,terminalCallbackVo);
        page.setRecords(list);
        return Response.successWithResult(page);
    }

}
