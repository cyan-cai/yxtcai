package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.model.param.IotPlatformInsertParam;
import com.java.yxt.model.param.IotPlatformQueryParam;
import com.java.yxt.model.param.IotPlatformUpdateParam;
import com.java.yxt.model.param.IotPlatformValidateServiceIdParam;
import com.java.yxt.model.vo.IotPlatformVo;
import com.java.yxt.service.ICommonService;
import com.java.yxt.service.IotPlatformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 
 * @Description: 平台管理
 * @author 李峰
 * @date 2020年11月12日 下午4:08:00
 */
@Tag(description = "平台管理", name = "平台管理")
@RestController
@Slf4j
@RequestMapping("/iotPlatform")
@Api(tags = { "平台管理" })
public class IotPlatformController {

	@Autowired
	IotPlatformService iotPlatformService;
	@Autowired
	ICommonService commonService;
	@PostMapping("/add")
	@ApiOperation("添加平台信息")
	@SOLog(type = OperationType.post, tableName = "mgt_iot_platform", remark = "添加平台信息")
	public Response<?> add(@RequestBody IotPlatformInsertParam iotPlatformVo, HttpServletRequest request) {
		Admin userInfo = commonService.getUserInfo(request);
		if (userInfo != null) {
			iotPlatformVo.setCreaterName(userInfo.getRealname());
		}
		log.info("调用添加平台信息接口，入参:", iotPlatformVo);
		return iotPlatformService.insert(iotPlatformVo);
	}

	@PostMapping("/update")
	@ApiOperation("修改平台信息")
	@SOLog(type = OperationType.put, tableName = "mgt_iot_platform", remark = "修改平台信息", mapperName = "iotPlatformMapper", paramKey = "id")
	public Response<?> update(@RequestBody IotPlatformUpdateParam iotPlatformVo, HttpServletRequest request) {
		Admin userInfo = commonService.getUserInfo(request);
		if (userInfo != null) {
			iotPlatformVo.setUpdaterName(userInfo.getName());
		}
		log.info("调用修改平台信息接口，入参:", iotPlatformVo);
		return iotPlatformService.update(iotPlatformVo);
	}

	/**
	 * 修改平台信息
	 * 
	 * @param word
	 * @return
	 */
	@ApiOperation("查询平台信息")
	@GetMapping("/info/{id}")
//	@SOLog(type = OperationType.get, tableName = "mgt_iot_platform", remark = "查询平台信息")
	public Response<?> info(@PathVariable Long id) {
		log.info("调用查询平台信息接口，入参:id=", id);
		return iotPlatformService.info(id);
	}

	/**
	 * 删除平台信息
	 * 
	 * @param word
	 * @return
	 */
	@ApiOperation("删除平台信息(支持单个、批量)")
	@PostMapping("/delete")
	@SOLog(type = OperationType.delete, tableName = "mgt_iot_platform", remark = "删除平台信息", mapperName = "iotPlatformMapper", paramKey = "id")
	@ApiImplicitParam(name = "idList", value = "", allowMultiple = true, paramType = "body", example = "[9,10]", defaultValue = "[9,11]", dataTypeClass = Long.class)
	public Response<?> delete(@RequestBody List<Long> idList) {
		log.info("调用删除平台信息接口，idList：", idList);
		return iotPlatformService.delete(idList);
	}

	/**
	 * 分页查询
	 * 
	 * @param word
	 * @return
	 */
	@ApiOperation("查询平台信息列表")
	@PostMapping("/list")
//	@SOLog(type = OperationType.get, tableName = "mgt_iot_platform", remark = "分页查询平台信息")
	public Response<IPage<IotPlatformVo>> list(@RequestBody IotPlatformQueryParam sensitiveWordQueryVO) {
		return iotPlatformService.selectByPage(sensitiveWordQueryVO);
	}

	/**
	 * 分页查询
	 * 
	 * @param word
	 * @return
	 */
	@ApiOperation("校验平台标识")
	@PostMapping("/validateServerId")
//	@SOLog(type = OperationType.get, tableName = "mgt_iot_platform", remark = "分页查询平台信息")
	public Response<?> validateServerId(@RequestBody IotPlatformValidateServiceIdParam validateParam) {
		return iotPlatformService.validateServerId(validateParam);
	}

}
