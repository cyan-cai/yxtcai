package com.java.yxt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.TerminalVersionProtocolService;
import com.java.yxt.util.SequenceWorker;
import com.java.yxt.vo.TerminalVersionProtocolVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zanglei
 * @version V1.0
 * @description 终端版本协议控制层
 * @Package com.java.yxt.controller
 * @date 2021/1/18
 */
@RestController
@Slf4j
@Api(tags = {"终端版本协议管理"})
@RequestMapping("/terminalversionprotocol")
public class TerminalVersionProtocolController {

    @Autowired
    TerminalVersionProtocolService terminalVersionProtocolService;

    /**
     * 添加
     * @param terminalVersionProtocolVo
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("添加终端版本协议")
    @SetCreaterName
    @SOLog(tableName = "mgt_terminal_version_protocolmanagement",remark = "添加终端版本协议" ,type = OperationType.post)
    public Response addTerminalVersionProtocol(@RequestBody TerminalVersionProtocolVo terminalVersionProtocolVo){
        SequenceWorker worker = new SequenceWorker(0,0,0);
        terminalVersionProtocolVo.setId(String.valueOf(worker.nextId()));
        terminalVersionProtocolService.addRecord(terminalVersionProtocolVo);
        return Response.successWithResult(terminalVersionProtocolVo.getId());
    }

    /**
     * 更新
     * @param terminalVersionProtocolVo
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("根据id更新终端版本协议")
    @SOLog(type = OperationType.put,tableName = "mgt_terminal_version_protocolmanagement",remark = "更新终端版本协议",
            mapperName = "terminalVersionProtocolMapper",paramKey = "id")
    public Response updateTerminalVersionProtocol(@RequestBody TerminalVersionProtocolVo terminalVersionProtocolVo){
        terminalVersionProtocolService.update(terminalVersionProtocolVo);
        return Response.success();
    }

    /**
     * 分页查询
     * @param terminalVersionProtocolVo
     * @return
     */
    @PostMapping("/selectbypage")
    @ApiOperation("分页查询终端版本协议")
    public Response selectByPageTerminalVersionProtocol(@RequestBody TerminalVersionProtocolVo terminalVersionProtocolVo){
        Page page = new Page(terminalVersionProtocolVo.getCurrent(),terminalVersionProtocolVo.getSize());
        return  Response.successWithResult(terminalVersionProtocolService.selectByPage(page,terminalVersionProtocolVo));
    }

    /**
     * 不分页查询
     * @param terminalVersionProtocolVo
     * @return
     */
    @PostMapping("/selectall")
    @ApiOperation("不分页查询所有记录")
    public Response selectAllTerminalVersionProtocol(@RequestBody TerminalVersionProtocolVo terminalVersionProtocolVo){
        return Response.successWithResult(terminalVersionProtocolService.selectAll(terminalVersionProtocolVo));
    }

    /**
     * 删除
     * @param terminalVersionProtocolVo
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation("删除终端版本协议记录")
    @SOLog(type = OperationType.put,tableName = "mgt_terminal_version_protocolmanagement",remark = "删除终端版本协议",
            mapperName = "terminalVersionProtocolMapper",paramKey = "id")
    public Response deleteTerminalVersionProtocol(@RequestBody TerminalVersionProtocolVo terminalVersionProtocolVo){
        return  Response.successWithResult(terminalVersionProtocolService.delete(terminalVersionProtocolVo));
    }

}
