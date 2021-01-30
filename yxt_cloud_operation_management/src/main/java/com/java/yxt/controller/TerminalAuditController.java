package com.java.yxt.controller;

import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.TerminalAuditService;
import com.java.yxt.service.TerminalDeviceService;
import com.java.yxt.vo.TerminalDeviceAuditDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * TerminalDeviceAuditController
 *
 * @author 蔡家明
 * @version V1.0
 * @description 终端设备审核管理
 * @Package com.java.yxt.controller
 * @date 2020/12/10
 */
@RestController
@Slf4j
@Api(tags = {"终端设备审核管理"})
@RequestMapping("terminalDeviceAudit")
public class TerminalAuditController {
    @Autowired
    TerminalAuditService terminalAuditService;
    @Autowired
    TerminalDeviceService terminalDeviceService;

    @PostMapping("/selectAuditDetail")
    @ApiOperation("查询终端设备审核详情")
    public Response selectAuditDetail(@RequestBody TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo) {
        List<TerminalDeviceAuditDetailVo> list=terminalAuditService.selectAuditDetail(terminalDeviceAuditDetailVo);
        return Response.successWithResult(list);
    }

    @PostMapping("/approve")
    @ApiOperation("审核通过")
    @SOLog(tableName = "mgt_device_audit_detail", remark = "审核通过" ,type = OperationType.put)
    public Response approve(@RequestBody TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo) {
        terminalAuditService.approve(terminalDeviceAuditDetailVo);
        return Response.successWithResult(terminalDeviceAuditDetailVo.getAuditId());
    }

    @PostMapping("/approveBatch")
    @ApiOperation("批量审核通过")
    @SOLog(tableName = "mgt_device_audit_detail", remark = "批量审核通过" ,type = OperationType.put)
    public Response approveBatch(@RequestBody List<TerminalDeviceAuditDetailVo> list, HttpServletRequest request) {
        terminalAuditService.approveBatch(list,request);
        return Response.success();
    }

    @PostMapping("/reject")
    @ApiOperation("审核驳回")
    @SOLog(tableName = "mgt_device_audit_detail", remark = "审核驳回" ,type = OperationType.put)
    public Response reject(@RequestBody TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo) {
        terminalAuditService.reject(terminalDeviceAuditDetailVo);
        return Response.successWithResult(terminalDeviceAuditDetailVo.getAuditId());
    }

    @PostMapping("/rejectBatch")
    @ApiOperation("批量审核驳回")
    @SOLog(tableName = "mgt_device_audit_detail", remark = "批量审核驳回" ,type = OperationType.put)
    public Response rejectBatch(@RequestBody List<TerminalDeviceAuditDetailVo> list, HttpServletRequest request) {
        terminalAuditService.rejectBatch(list,request);
        return Response.success();
    }

    @PostMapping("/approveDelete")
    @ApiOperation("批准删除")
    @SOLog(tableName = "mgt_device_audit_detail", remark = "批准删除" ,type = OperationType.put)
    public Response approveDelete(@RequestBody TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo) {
        terminalAuditService.approveDelete(terminalDeviceAuditDetailVo);
        return Response.successWithResult(terminalDeviceAuditDetailVo.getAuditId());
    }

    @PostMapping("/rejectDelete")
    @ApiOperation("驳回删除")
    @SOLog(tableName = "mgt_device_audit_detail", remark = "驳回删除" ,type = OperationType.put)
    public Response rejectDelete(@RequestBody TerminalDeviceAuditDetailVo terminalDeviceAuditDetailVo) {
        terminalAuditService.rejectDelete(terminalDeviceAuditDetailVo);
        return Response.successWithResult(terminalDeviceAuditDetailVo.getAuditId());
    }
}
