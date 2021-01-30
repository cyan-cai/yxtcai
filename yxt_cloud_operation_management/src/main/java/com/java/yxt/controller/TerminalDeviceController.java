package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.TerminalDeviceService;
import com.java.yxt.vo.TerminalDeviceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * TerminalDeviceController
 *
 * @author 蔡家明
 * @version V1.0
 * @description 终端设备管理
 * @Package com.java.yxt.controller
 * @date 2020/12/08
 */

@RestController
@Slf4j
@Api(tags = {"终端设备管理"})
@RequestMapping("terminalDevice")
public class TerminalDeviceController {
    @Autowired
    TerminalDeviceService terminalDeviceService;

    @PostMapping("/add")
    @ApiOperation("添加终端设备")
    @SOLog(tableName = "mgt_terminal_device", remark = "添加终端设备" ,type = OperationType.post)
    public Response addTerminalDevice(@RequestBody TerminalDeviceVo terminalDeviceVo,HttpServletRequest request) {
        terminalDeviceService.insert(terminalDeviceVo,request);
        return Response.successWithResult(terminalDeviceVo.getId());
    }

    @PostMapping("/update")
    @ApiOperation("更新终端设备")
    @SOLog(tableName = "mgt_terminal_device", remark = "更新终端设备" ,type = OperationType.put)
    public Response updateTerminalDevice(@RequestBody TerminalDeviceVo terminalDeviceVo) {
        terminalDeviceService.update(terminalDeviceVo);
        return Response.success();
    }

    @PostMapping("/updateBatch")
    @ApiOperation("批量提交审核")
    @SOLog(tableName = "mgt_terminal_device", remark = "批量提交审核" ,type = OperationType.put)
    public Response updateBatchTerminalDevice(@RequestBody List<TerminalDeviceVo> list) {
        terminalDeviceService.updateBatch(list);
        return Response.success();
    }

    @PostMapping("/delete")
    @ApiOperation("删除终端设备")
    @SOLog(tableName = "mgt_terminal_device", remark = "删除终端设备" ,type = OperationType.delete,paramKey = "id")
    public Response deleteTerminalDevice(@RequestBody TerminalDeviceVo terminalDeviceVo) {
        terminalDeviceService.delete(terminalDeviceVo);
        return Response.success();
    }

    @PostMapping("/deleteApproved")
    @ApiOperation("申请删除通过审核的终端设备")
    @SOLog(tableName = "mgt_terminal_device", remark = "申请删除通过审核的终端设备" ,type = OperationType.put)
    public Response deleteAuditTerminalDevice(@RequestBody TerminalDeviceVo terminalDeviceVo) {
        terminalDeviceVo.setAuditStatus(5);
        terminalDeviceService.update(terminalDeviceVo);
        return Response.success();
    }

    @PostMapping("/select")
    @ApiOperation("分页查询终端设备")
    public Response selectTerminalDevice(@RequestBody TerminalDeviceVo terminalDeviceVo,HttpServletRequest request) {
        Page<TerminalDeviceVo> page = new Page<>(terminalDeviceVo.getCurrent(),terminalDeviceVo.getSize());
        List<OrderItem> orderItems = new ArrayList<>(2);
        orderItems.add(OrderItem.desc("mtd.create_time"));
        orderItems.add(OrderItem.asc("mtd.imei"));
        page.setOrders(orderItems);
        page.setRecords(terminalDeviceService.select(page,terminalDeviceVo,request));
        return Response.successWithResult(page);
    }

    @PostMapping("/selectAll")
    @ApiOperation("不分页查询终端设备")
    public Response selectAllTerminalDevice(@RequestBody TerminalDeviceVo terminalDeviceVo) {
        List<TerminalDeviceVo> list=terminalDeviceService.selectAll(terminalDeviceVo);
        return Response.successWithResult(list);
    }

    @PostMapping(value = "/uploadTerminalDevice",consumes = "multipart/*" ,headers = "content-type=multipart/form-data")
    @ApiOperation("终端设备导入")
    @SOLog(tableName = "mgt_terminal_device",remark = "终端设备导入" )
    public Response uploadCustomer(MultipartFile file,@RequestParam String createrId,HttpServletResponse httpServletResponse,HttpServletRequest request) {
        Response response=terminalDeviceService.uploadTerminalDevice(file,createrId,httpServletResponse,request);
        return response;
    }

    @GetMapping("/export")
    @ApiOperation("批量导出")
    @SOLog(tableName = "mgt_terminal_device",remark = "终端设备导出" )
    public Response export(TerminalDeviceVo terminalDeviceVo, HttpServletResponse response){
        terminalDeviceService.export(terminalDeviceVo,response);
        return Response.success();
    }

    @GetMapping("/exportError")
    @ApiOperation("批量导出异常信息")
    @SOLog(tableName = "mgt_terminal_device",remark = "终端设备异常信息导出" )
    public Response exportError(TerminalDeviceVo terminalDeviceVo,String key,HttpServletResponse response){
        terminalDeviceService.exportError(key,response);
        return Response.success();
    }


    @PostMapping("/checkUser")
    @ApiOperation("检查用户权限判断是否是终端厂商")
    public Response checkUser(HttpServletRequest request){
        Response response=terminalDeviceService.checkUser(request);
        return response;
    }

}
