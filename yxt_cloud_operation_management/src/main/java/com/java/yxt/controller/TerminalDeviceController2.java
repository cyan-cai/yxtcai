package com.java.yxt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.po.TerminalDevicePo2;
import com.java.yxt.service.ITerminalDeviceService;
import com.java.yxt.vo.TermainDeviceQueryVo;
import com.java.yxt.vo.TerminalDeviceVo2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 
 * @Description: 终端设备管理
 * @author 李峰
 * @date 2020年9月28日 下午3:07:40
 */

@Slf4j
@Api(tags = { "终端设备管理" })
@RequestMapping("/terminalDevice")
public class TerminalDeviceController2 {
	@Autowired
	ITerminalDeviceService terminalDeviceService;

	@PostMapping("/add")
	@ApiOperation("添加用户终端设备")
	public Response addTerminal(@RequestBody TerminalDevicePo2 terminalDevicePo) {
		log.info("调用添加终端设备接口，请求参数:", terminalDevicePo);
		return terminalDeviceService.insert(terminalDevicePo);
	}

	@PostMapping("/update")
	@ApiOperation("更新终端设备")
	public Response updateTerminal(@RequestBody TerminalDevicePo2 terminalDevicePo) {
		return terminalDeviceService.update(terminalDevicePo);
	}

	@PostMapping("/list")
	@ApiOperation("分页查询终端设备")
	public Response<Page<TerminalDeviceVo2>> list(@RequestBody TermainDeviceQueryVo termainDeviceQueryVo) {
		return terminalDeviceService.selectByPage(termainDeviceQueryVo);
	}

	@ApiOperation(value = "删除终端设备")
	@PostMapping("/delete")
	@ApiImplicitParam(name = "idList", allowMultiple = true, paramType = "body", example = "[9,10]", defaultValue = "[9,11]", dataTypeClass = Long.class)
	public Response<?> delete(@RequestBody List<Long> idList) {
		return terminalDeviceService.delete(idList);
	}

	@ApiOperation(value = "审核批量通过")
	@PostMapping("/pass")
	@ApiImplicitParam(name = "idList", allowMultiple = true, paramType = "body", example = "[9,10]", defaultValue = "[9,11]", dataTypeClass = Long.class)
	public Response<?> pass(@RequestBody List<Long> idList) {
		return terminalDeviceService.pass(idList);
	}

	@ApiOperation(value = "审核批量拒绝")
	@PostMapping("/reject")
	@ApiImplicitParam(name = "idList", allowMultiple = true, paramType = "body", example = "[9,10]", defaultValue = "[9,11]", dataTypeClass = Long.class)
	public Response<?> reject(@RequestBody List<Long> idList) {
		return terminalDeviceService.reject(idList);
	}
}
