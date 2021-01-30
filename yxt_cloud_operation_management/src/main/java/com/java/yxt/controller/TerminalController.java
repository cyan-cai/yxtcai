package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.service.CustomerService;
import com.java.yxt.service.DictService;
import com.java.yxt.service.ICommonService;
import com.java.yxt.service.TerminalService;
import com.java.yxt.util.DateTimeUtil;
import com.java.yxt.vo.RelationTerminalVo;
import com.java.yxt.vo.TerminalVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TerminalController
 *
 * @author zanglei
 * @version V1.0
 * @description 用户终端管理
 * @Package com.java.yxt.controller
 * @date 2020/9/15
 */
@RestController
@Slf4j
@Api(tags = { "终端用户管理" })
@RequestMapping("terminal")
public class TerminalController {

	@Autowired
	TerminalService terminalService;

	@Autowired
	CustomerService customerService;
	@Autowired
	ICommonService commonService;
	@Autowired
	DictService dictService;

	@PostMapping("/add")
	@ApiOperation("添加终端用户")
	@SOLog(tableName = "mgt_terminal", remark = "添加终端用户" ,type = OperationType.post)
	public Response addTerminal(@RequestBody TerminalVo terminalVo) {
		terminalService.insert(terminalVo);
		return Response.successWithResult(terminalVo.getId());
	}

	@PostMapping("/update")
	@ApiOperation("更新用户终端信息")
	public Response updateTerminal(@RequestBody List<TerminalVo> terminalVos) {
		terminalVos.stream().forEach(terminalVo -> {
			terminalVo.setUpdateTime(DateTimeUtil.localDateTimeConvertToDate(LocalDateTime.now()));
			terminalService.update(terminalVo);
		});
		return Response.success();
	}

	@PostMapping("/relationcustomer")
	@ApiOperation("终端关联客户")
	@SOLog(type = OperationType.post,remark = "客户关联终端",tableName = "mgt_terminal")
	public Response relationCustomer(@RequestBody RelationTerminalVo relationTerminalVo) {
		terminalService.relationCustomer(relationTerminalVo);
		return Response.success();
	}

	@PostMapping("/unRelationCustomer")
	@ApiOperation("未关联客户的终端信息")
	public Response unRelationCustomer(@RequestBody TerminalVo terminalVo) {
		Page<TerminalVo> page = new Page<>(terminalVo.getCurrent(),terminalVo.getSize());
		List<OrderItem> orderItems= Lists.newArrayList();
		orderItems.add(OrderItem.desc("create_time"));
		page.setOrders(orderItems);
		List<TerminalVo> list = terminalService.unRelationCustomer(page,terminalVo);
		page.setRecords(list);
		return Response.successWithResult(page);
	}

	@PostMapping("/selectall")
	@ApiOperation("根据条件查询所有终端信息，不分页")
	public Response selectAll(@RequestBody TerminalVo terminalVo) {

		List<TerminalVo> list = terminalService.selectAll(terminalVo);
		return Response.successWithResult(list);
	}

	@PostMapping("/select")
	@ApiOperation("分页查询用户终端信息")
	public Response selectTerminal(@RequestBody TerminalVo terminalVo, HttpServletRequest request) {
		Admin userInfo = commonService.getUserInfo(request);
		if (userInfo != null && userInfo.getOrgId() != null&&userInfo.getOrgId() != 0) {
			terminalVo.setCustomerId(userInfo.getOrgId() + "");
		}
		if (null == terminalVo.getCurrent() && null == terminalVo.getSize()) {
			List<TerminalVo> list = terminalService.select(null, terminalVo);
			return Response.successWithResult(list);
		}
		Page<TerminalVo> page = new Page(terminalVo.getCurrent(), terminalVo.getSize());
		List<OrderItem> orderItems = new ArrayList<>(1);
		orderItems.add(OrderItem.desc("mt.create_time"));
		page.setOrders(orderItems);
		List<TerminalVo> list = terminalService.select(page, terminalVo);
		page.setRecords(list);
		return Response.successWithResult(page);
	}

	@PostMapping("/selectcustomerbymsisdn")
	@ApiOperation("根据终端号码查询客户终端信息")
	public Response selectCustomerByMsisdn(@RequestBody TerminalVo terminalVo) {
		List<TerminalVo> list = terminalService.selectcustomerbymsisdn(terminalVo);
		return Response.successWithResult(list);
	}

	@PostMapping("/selectdistinctcustomerbymsisdn")
	@ApiOperation("根据终端号码查询客户终端信息")
	public Response selectDistinctCustomerByMsisdn(@RequestBody TerminalVo terminalVo) {
		List<TerminalVo> list = terminalService.selectdistinctcustomerbymsisdn(terminalVo);
		return Response.successWithResult(list);
	}

	@PostMapping(value = "/uploadterminaluser", consumes = "multipart/*", headers = "content-type=multipart/form-data")
	@ApiOperation("终端用户导入")
	@SOLog(tableName = "mgt_terminal",remark = "终端用户导入")
	public Response uploadTerminalUser(MultipartFile file, @RequestParam String createrId,HttpServletResponse httpServletResponse,HttpServletRequest request) {
		Response response=terminalService.uploadTerminalUser(file, createrId,httpServletResponse,request);
		return response;
	}

	@GetMapping("/export")
	@ApiOperation("批量导出")
	@SOLog(tableName = "mgt_terminal",remark = "终端用户导出" )
	public Response export(TerminalVo terminalVo, HttpServletResponse response) {
		terminalService.export(terminalVo, response);
		return Response.success();
	}

	@GetMapping("/exportError")
	@ApiOperation("批量导出异常信息")
	@SOLog(tableName = "mgt_terminal",remark = "终端用户异常信息导出" )
	public Response exportError(TerminalVo terminalVo,String key,HttpServletResponse response) {
		terminalService.exportError(key,response);
		return Response.success();
	}


}
