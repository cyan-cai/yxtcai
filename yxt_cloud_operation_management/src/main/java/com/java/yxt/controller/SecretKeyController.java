package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.SecretKeyService;
import com.java.yxt.vo.SecretKeyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * TerminalDeviceController
 *
 * @author 蔡家明
 * @version V1.0
 * @description 密钥管理
 * @Package com.java.yxt.controller
 * @date 2021/1/12
 */

@RestController
@Slf4j
@Api(tags = {"密钥管理"})
@RequestMapping("secretKey")
public class SecretKeyController {
    @Autowired
    SecretKeyService secretKeyService;


    @PostMapping("/select")
    @ApiOperation("分页查询")
    public Response selectTerminalDevice(@RequestBody SecretKeyVo secretKeyVo) {
        Page<SecretKeyVo> page = new Page<>(secretKeyVo.getCurrent(),secretKeyVo.getSize());
        List<OrderItem> orderItems = new ArrayList<>(1);
        page.setOrders(orderItems);
        page.setRecords(secretKeyService.select(page,secretKeyVo));
        return Response.successWithResult(page);
    }

    @PostMapping("/selectAll")
    @ApiOperation("不分页查询")
    public Response selectAllTerminalDevice(@RequestBody SecretKeyVo secretKeyVo) {
        List<SecretKeyVo> list=secretKeyService.selectAll(secretKeyVo);
        return Response.successWithResult(list);
    }

    @PostMapping("/add")
    @ApiOperation("添加密钥")
    @SOLog(tableName = "mgt_secret_key", remark = "添加密钥" ,type = OperationType.post)
    public Response addSecretKey(@RequestBody SecretKeyVo secretKeyVo) {
        secretKeyService.insert(secretKeyVo);
        return Response.successWithResult(secretKeyVo.getId());
    }

    @PostMapping("/update")
    @ApiOperation("更新密钥")
    @SOLog(tableName = "mgt_secret_key", remark = "更新密钥" ,type = OperationType.put)
    public Response updateSecretKey(@RequestBody SecretKeyVo secretKeyVo) {
        secretKeyService.update(secretKeyVo);
        return Response.successWithResult(secretKeyVo.getId());
    }

    @PostMapping("/enableAndDisable")
    @ApiOperation("启用/禁用密钥")
    @SOLog(tableName = "mgt_secret_key", remark = "启用/禁用密钥" ,type = OperationType.put)
    public Response enableAndDisableSecretKey(@RequestBody SecretKeyVo secretKeyVo) {
        secretKeyService.enableAndDisable(secretKeyVo);
        return Response.success();
    }


//    @PostMapping("/enable")
//    @ApiOperation("启用密钥")
//    @SOLog(tableName = "mgt_secret_key", remark = "启用密钥" ,type = OperationType.post)
//    public Response enableSecretKey(@RequestBody SecretKeyVo secretKeyVo) {
//        secretKeyService.enable(secretKeyVo);
//        return Response.success();
//    }
//
//    @PostMapping("/disable")
//    @ApiOperation("禁用密钥")
//    @SOLog(tableName = "mgt_secret_key", remark = "禁用密钥" ,type = OperationType.post)
//    public Response disableSecretKey(@RequestBody SecretKeyVo secretKeyVo) {
//        secretKeyService.disable(secretKeyVo);
//        return Response.success();
//    }



//    @PostMapping("/selectKeyHistory")
//    @ApiOperation("查询密钥历史记录")
//    @SOLog(tableName = "mgt_key_history", remark = "查询密钥历史记录" ,type = OperationType.post)
//    public Response selectKeyHistory(@RequestBody SecretKeyVo secretKeyVo) {
//        List<SecretKeyHistoryVo> list=secretKeyService.selectAllKeyHistory(secretKeyVo);
//        return Response.successWithResult(list);
//    }

}
