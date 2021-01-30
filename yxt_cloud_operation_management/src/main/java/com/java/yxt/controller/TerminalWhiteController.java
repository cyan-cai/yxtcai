package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.*;
import com.java.yxt.vo.ServiceVo;
import com.java.yxt.vo.TerminalWhiteVo;
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
 * TerminalWhiteController
 *
 * @author zanglei
 * @version V1.0
 * @description 终端白名单
 * @Package com.java.yxt.controller
 * @date 2020/9/19
 */
@RestController
@Slf4j
@RequestMapping("terminalwhite")
@Api(tags = {"终端白名单管理"})
public class TerminalWhiteController {
    @Autowired
    TerminalWhiteService terminalWhiteService;

    @Autowired
    BusinessService businessService;


    @Autowired
    TerminalCallbackService terminalCallbackService;

    @Autowired
    Apiservice apiservice;

    @Autowired
    ProductionApiRelationService productionApiRelationService;

    @Autowired
    DictService dictService;

    @Autowired
    TerminalService terminalService;

    @PostMapping("/selectservicecode")
    @ApiOperation("终端用户白名单-查询客户业务标识")
    public Response selectServiceCode(@RequestBody TerminalWhiteVo terminalWhiteVo){
        List<ServiceVo> list=terminalWhiteService.selectServiceCode(terminalWhiteVo);
        return Response.successWithResult(list);
    }

    @PostMapping("/add")
    @ApiOperation("添加终端白名单")
    @SetCreaterName
    @SOLog(tableName = "mgt_terminal_white",remark = "添加终端用户白名单" ,type = OperationType.post)
    public Response addTerminalWhite(@RequestBody List<TerminalWhiteVo> terminalWhiteVos){
        terminalWhiteVos.stream().forEach(terminalWhiteVo -> {
            try {
                terminalWhiteService.insert(terminalWhiteVo);
            } catch (JsonProcessingException e) {
                return;
            }
        });
        return Response.success();
    }


    @PostMapping("/update")
    @ApiOperation("修改终端白名单信息")
    @SetCreaterName
    @SOLog(type = OperationType.put,tableName = "mgt_terminal_white",remark = "更新终端用户白名单",
            mapperName = "terminalMapper",paramKey = "id")
    public Response updateTerminalWhite(@RequestBody TerminalWhiteVo terminalWhiteVo) throws JsonProcessingException {
        terminalWhiteService.update(terminalWhiteVo);
        return Response.success();
    }

    @PostMapping("/select")
    @ApiOperation("分页查询终端白名单信息")
    public Response selectTerminalWhite(@RequestBody TerminalWhiteVo terminalWhiteVo)
    {
        Page<TerminalWhiteVo> page = new Page<>(terminalWhiteVo.getCurrent(),terminalWhiteVo.getSize());
        List<OrderItem> orderItems = new ArrayList<>(1);
        orderItems.add(OrderItem.desc("c.create_time"));
        page.setOrders(orderItems);
        List<TerminalWhiteVo> list = terminalWhiteService.select(page,terminalWhiteVo);
        page.setRecords(list);
        return Response.successWithResult(page);
    }

    @PostMapping("/selectall")
    @ApiOperation("分页查询终端白名单信息")
    public Response selectAllTerminalWhite(@RequestBody TerminalWhiteVo terminalWhiteVo)
    {
        List<TerminalWhiteVo> list = terminalWhiteService.selectAll(terminalWhiteVo);
        return Response.successWithResult(list);
    }

    @PostMapping(value = "/uploadterminalwhite",consumes = "multipart/*" ,headers = "content-type=multipart/form-data")
    @ApiOperation("终端用户导入")
    @SOLog(tableName = "mgt_terminal_white",remark = "终端用户白名单导入" )
    public Response uploadterminalwhite(MultipartFile file, @RequestParam String createrId, HttpServletResponse httpServletResponse, HttpServletRequest request){
        Response response=terminalWhiteService.uploadterminalwhite(file, createrId,httpServletResponse,request);
        return response;
    }


    @GetMapping("/export")
    @ApiOperation("批量导出")
    @SOLog(tableName = "mgt_terminal_white",remark = "终端用户白名单导出" )
    public Response export(TerminalWhiteVo  terminalWhiteVo, HttpServletResponse response){
       terminalWhiteService.export(terminalWhiteVo, response);
        return Response.success();
    }

    @GetMapping("/exportError")
    @ApiOperation("批量导出异常信息")
    @SOLog(tableName = "mgt_terminal_white",remark = "终端用户白名单导出异常信息" )
    public Response exportError(TerminalWhiteVo  terminalWhiteVo, String key,HttpServletResponse response){
        terminalWhiteService.exportError(key,response);
        return Response.success();
    }
}
