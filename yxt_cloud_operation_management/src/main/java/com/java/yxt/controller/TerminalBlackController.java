package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.BusinessService;
import com.java.yxt.service.TerminalBlackService;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.ServiceVo;
import com.java.yxt.vo.TerminalBlackVo;
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
 * TerminalBlackController
 *
 * @author zanglei
 * @version V1.0
 * @description 终端黑名单管理
 * @Package com.java.yxt.controller
 * @date 2020/9/19
 */
@RestController
@RequestMapping("terminalblack")
@Api(tags = {"黑名单管理"})
@Slf4j
public class TerminalBlackController {
    @Autowired
    TerminalBlackService terminalBlackService;

    @Autowired
    BusinessService businessService;

    @PostMapping("/selectbycustomername")
    @ApiOperation("添加黑名单-SP平台查询有效客户名称")
    public Response selectByCustomerName(@RequestBody TerminalBlackVo terminalBlackVo){
        List<TerminalBlackVo> list=terminalBlackService.selectByCustomerName(terminalBlackVo);
        return Response.successWithResult(list);
    }

    @PostMapping("/selectservicecode")
    @ApiOperation("添加黑名单-SP平台查询客户业务标识")
    public Response selectServiceCode(@RequestBody TerminalBlackVo terminalBlackVo){
        List<ServiceVo> list=terminalBlackService.selectServiceCode(terminalBlackVo);
        return Response.successWithResult(list);
    }


    @PostMapping("/adduser")
    @ApiOperation("添加黑名单-终端用户")
    @SetCreaterName
    @SOLog(tableName = "mgt_terminal_black",remark = "添加黑名单终端用户" ,type = OperationType.post)
    public Response addTerminalBlackUser(@RequestBody TerminalBlackVo terminalBlackVo){
        terminalBlackService.insertUser(terminalBlackVo);
        return Response.successWithResult(terminalBlackVo.getId());
    }

    @PostMapping("/addsp")
    @ApiOperation("添加黑名单-SP应用平台")
    @SetCreaterName
    @SOLog(tableName = "mgt_terminal_black",remark = "添加黑名单SP应用平台" ,type = OperationType.post)
    public Response addTerminalBlackSp(@RequestBody TerminalBlackVo terminalBlackVo){
        terminalBlackVo.setServiceCode(terminalBlackVo.getServiceCodes().get(0));
        terminalBlackService.insertSp(terminalBlackVo);
        return Response.successWithResult(terminalBlackVo.getId());
    }


    @PostMapping("/updateuser")
    @ApiOperation("修改黑名单终端用户")
    @SOLog(type = OperationType.put,tableName = "mgt_terminal_black",remark = "更新黑名单终端用户",mapperName = "terminalBlackMapper",
            paramKey = "id")
    public Response updateTerminalBlackUser(@RequestBody TerminalBlackVo terminalBlackVo){
        terminalBlackService.updateUser(terminalBlackVo);
        return Response.success();
    }

    @PostMapping("/updatesp")
    @ApiOperation("修改黑名单SP应用平台")
    @SOLog(type = OperationType.put,tableName = "mgt_terminal_black",remark = "更新黑名单SP应用平台",mapperName = "terminalBlackMapper",
            paramKey = "id")
    public Response updateTerminalBlackSp(@RequestBody TerminalBlackVo terminalBlackVo){
        terminalBlackService.updateSp(terminalBlackVo);
        return Response.success();
    }

    @PostMapping("/selectuser")
    @ApiOperation("分页查询黑名单终端用户")
    public Response selectTerminalBlackUser(@RequestBody TerminalBlackVo terminalBlackVo)
    {	
    	if(ValidateUtil.isNotEmpty(terminalBlackVo.getCreateTimeList())){
            terminalBlackVo.setCreateBeginTime((String)terminalBlackVo.getCreateTimeList().toArray()[0]);
            terminalBlackVo.setCreateEndTime((String)terminalBlackVo.getCreateTimeList().toArray()[1]);
        }
    	 Page<TerminalBlackVo> page =new Page<>(terminalBlackVo.getCurrent(),terminalBlackVo.getSize());
         List<OrderItem> orderItems = new ArrayList<>(1);
         orderItems.add(OrderItem.desc("mtb.create_time"));
         page.setOrders(orderItems);
         List<TerminalBlackVo> list = terminalBlackService.selectUser(page,terminalBlackVo);
         page.setRecords(list);
         return Response.successWithResult(page);
    }

    @PostMapping("/selectsp")
    @ApiOperation("分页查询黑名单终端用户")
    public Response selectTerminalBlackSp(@RequestBody TerminalBlackVo terminalBlackVo)
    {
        if(ValidateUtil.isNotEmpty(terminalBlackVo.getCreateTimeList())){
            terminalBlackVo.setCreateBeginTime((String)terminalBlackVo.getCreateTimeList().toArray()[0]);
            terminalBlackVo.setCreateEndTime((String)terminalBlackVo.getCreateTimeList().toArray()[1]);
        }
        Page<TerminalBlackVo> page =new Page<>(terminalBlackVo.getCurrent(),terminalBlackVo.getSize());
        List<OrderItem> orderItems = new ArrayList<>(1);
        orderItems.add(OrderItem.desc("mtb.create_time"));
        page.setOrders(orderItems);
        List<TerminalBlackVo> list = terminalBlackService.selectSp(page,terminalBlackVo);
        page.setRecords(list);
        return Response.successWithResult(page);
    }

    @PostMapping("enableordisableuser")
    @ApiOperation("黑名单终端用户启用禁用")
    @SOLog(type = OperationType.put,tableName = "mgt_terminal_black",remark = "黑名单终端用户启用禁用",mapperName = "terminalBlackMapper",
            paramKey = "id")
    public Response enableDisAbleUser(@RequestBody TerminalBlackVo terminalBlackVo){
        terminalBlackService.enableDisable(terminalBlackVo);
        return Response.success();
    }

    @PostMapping("enableordisablesp")
    @ApiOperation("黑名单SP应用平台启用禁用")
    @SOLog(type = OperationType.put,tableName = "mgt_terminal_black",remark = "黑名单SP应用平台启用禁用",mapperName = "terminalBlackMapper",
            paramKey = "id")
    public Response enableDisAbleSp(@RequestBody TerminalBlackVo terminalBlackVo){
        terminalBlackService.enableDisable(terminalBlackVo);
        return Response.success();
    }

    @PostMapping("unrelationblackservicecode")
    @ApiOperation("未被关联的sp客户黑名单业务标识")
    public Response unRelationBlackServiceCode(@RequestBody TerminalBlackVo terminalBlackVo){
        List<ServiceVo> list = businessService.unRelationBlackServiceCode(terminalBlackVo);

        return Response.successWithResult(list);
    }

    @PostMapping(value = "/uploadterminalblackuser",consumes = "multipart/*" ,headers = "content-type=multipart/form-data")
    @ApiOperation("黑名单终端用户导入")
    @SOLog(tableName = "mgt_terminal_black",remark = "黑名单终端用户导入")
    public Response uploadTerminalBlackUser(MultipartFile file, @RequestParam String createrId, HttpServletResponse httpServletResponse, HttpServletRequest request){
        Response response=terminalBlackService.uploadTerminalBlackUser(file,createrId,httpServletResponse,request);
        return response;
    }

    @PostMapping(value = "/uploadterminalblacksp",consumes = "multipart/*" ,headers = "content-type=multipart/form-data")
    @ApiOperation("黑名单SP平台导入")
    @SOLog(tableName = "mgt_terminal_black",remark = "黑名单SP应用平台导入")
    public Response uploadTerminalBlackSp(MultipartFile file, @RequestParam String createrId,HttpServletResponse httpServletResponse,HttpServletRequest request){
        Response response=terminalBlackService.uploadTerminalBlackSp(file,createrId,httpServletResponse,request);
        return response;
    }

    @GetMapping("/exportuser")
    @ApiOperation("黑名单终端用户批量导出")
    @SOLog(tableName = "mgt_terminal_black",remark = "黑名单终端用户导出" )
    public Response exportUser(TerminalBlackVo terminalBlackVo , HttpServletResponse response){
        if(ValidateUtil.isNotEmpty(terminalBlackVo.getCreateTimeList())){
            terminalBlackVo.setCreateBeginTime((String)terminalBlackVo.getCreateTimeList().toArray()[0]);
            terminalBlackVo.setCreateEndTime((String)terminalBlackVo.getCreateTimeList().toArray()[1]);
        }
        terminalBlackService.exportUser(terminalBlackVo,response);
        return Response.success();
    }

    @GetMapping("/exportuserError")
    @ApiOperation("黑名单终端用户异常信息批量导出")
    @SOLog(tableName = "mgt_terminal_black",remark = "黑名单终端用户异常信息导出" )
    public Response exportUserError(TerminalBlackVo terminalBlackVo ,String key,HttpServletResponse response){
        terminalBlackService.exportUserError(key,response);
        return Response.success();
    }

    @GetMapping("/exportsp")
    @ApiOperation("黑名单SP客户批量导出")
    @SOLog(tableName = "mgt_terminal_black",remark = "黑名单SP应用平台导出" )
    public Response exportSp(TerminalBlackVo terminalBlackVo , HttpServletResponse response){
        if(ValidateUtil.isNotEmpty(terminalBlackVo.getCreateTimeList())){
            terminalBlackVo.setCreateBeginTime((String)terminalBlackVo.getCreateTimeList().toArray()[0]);
            terminalBlackVo.setCreateEndTime((String)terminalBlackVo.getCreateTimeList().toArray()[1]);
        }
        terminalBlackService.exportSp(terminalBlackVo,response);
        return Response.success();
    }

    @GetMapping("/exportspError")
    @ApiOperation("黑名单SP客户批量导出异常信息")
    @SOLog(tableName = "mgt_terminal_black",remark = "黑名单SP应用平台导出异常信息" )
    public Response exportSpError(TerminalBlackVo terminalBlackVo , String key,HttpServletResponse response){
        terminalBlackService.exportSpError(key,response);
        return Response.success();
    }
}
