package com.java.yxt.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import com.java.yxt.annotation.SetCreaterName;
import com.java.yxt.base.Response;
import com.java.yxt.constant.RedisKeyConst;
import com.java.yxt.dto.CustomerInfo;
import com.java.yxt.logger.OperationType;
import com.java.yxt.logger.SOLog;
import com.java.yxt.service.BusinessService;
import com.java.yxt.service.CustomerService;
import com.java.yxt.service.DictService;
import com.java.yxt.service.impl.CustomerTenantServiceImpl;
import com.java.yxt.util.JsonUtil;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.CustomerTenantVo;
import com.java.yxt.vo.CustomerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * CustomerController
 *
 * @author zanglei
 * @version V1.0
 * @description 客户管理
 * @Package com.java.yxt.controller
 * @date 2020/9/15
 */
@RestController
@Slf4j
@RequestMapping("customer")
@Api(tags = {"客户管理"})
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    BusinessService businessService;

    @Autowired
    DictService dictService;

    @Autowired
    RedissonClient redissonUtils;

    @Autowired
    CustomerTenantServiceImpl customerTenantService;

    @PostMapping("/add")
    @ApiOperation("添加客户")
    @SetCreaterName
    @SOLog(tableName = "mgt_customer", remark = "添加客户",type = OperationType.post)
    public Response addCustomer(@RequestBody CustomerVo customerVo, HttpServletRequest request){
        customerService.insert(customerVo);
        return Response.successWithResult(customerVo);
    }

    @PostMapping("/update")
    @ApiOperation("更新客户信息")
    @SetCreaterName
    @SOLog(type = OperationType.put,tableName = "mgt_customer",remark = "更新客户信息",mapperName = "customerMapper",paramKey = "id")
    public Response updateCustomer(@RequestBody CustomerVo customerVo){
        return Response.successWithResult(customerService.update(customerVo));
    }

    @PostMapping("/enabledisable")
    @ApiOperation("启用禁用")
    @SetCreaterName
    @SOLog(type = OperationType.put,tableName = "mgt_customer",remark = "客户启用禁用",mapperName = "customerMapper",paramKey = "id")
    public Response enableDisable(@RequestBody CustomerVo customerVo) throws JsonProcessingException {
        int count = customerService.updateStatus(customerVo);
        // 同步redis状态
        RBucket<String> customerServiceCode  =  redissonUtils.getBucket(RedisKeyConst.GATEWAY_CUSTOMER_INFO_DIR+customerVo.getCustomerCode());
        if(ValidateUtil.isNotEmpty(customerServiceCode.get())){
            CustomerInfo customerInfo = JsonUtil.json2Object(customerServiceCode.get(),CustomerInfo.class);
            customerInfo.setStatus(customerVo.getStatus());
            customerServiceCode.set(JsonUtil.objectToJson(customerInfo));
        }
        return Response.successWithResult(count);
    }

    @PostMapping("/select")
    @ApiOperation("分页查询客户信息")
    public Response selectCustomer(@RequestBody CustomerVo customerVo){
        Page<CustomerVo> page = new Page<>(customerVo.getCurrent(),customerVo.getSize());
        List<OrderItem> orderItems= Lists.newArrayList();
        orderItems.add(OrderItem.desc("create_time"));
        page.setOrders(orderItems);
        page.setRecords(customerService.select(page,customerVo));
        return Response.successWithResult(page);
    }


    @PostMapping("/selectAll")
    @ApiOperation("查询客户信息")
    public Response selectAllCustomer(@RequestBody CustomerVo customerVo){
        List<CustomerVo> list = customerService.selectAll(customerVo);
        return Response.successWithResult(list);
    }

    @PostMapping("/selectAllValid")
    @ApiOperation("查询所有客户状态有效的客户信息")
    public Response selectAllValidCustomer(@RequestBody CustomerVo customerVo){
        customerVo.setStatus(1);
        List<CustomerVo> list = customerService.selectAll(customerVo);
        return Response.successWithResult(list);
    }

    @PostMapping("/selectAllValidShortMsg")
    @ApiOperation("查询所有有效的开通短报文的客户信息")
    public Response selectAllValidShortMsgCustomer(@RequestBody CustomerVo customerVo){
        List<CustomerVo> list = customerService.selectAllValidShortMsg(customerVo);
        return Response.successWithResult(list);
    }

    @PostMapping(value = "/uploadcustomer",consumes = "multipart/*" ,headers = "content-type=multipart/form-data")
    @ApiOperation("客户信息导入")
    @SOLog(tableName = "mgt_customer",remark = "客户管理导入" )
    public Response uploadCustomer(MultipartFile file,@RequestParam String createrId,HttpServletResponse httpServletResponse,HttpServletRequest request){
        Response  response=customerService.uploadCustomer(file,createrId,httpServletResponse,request);
        return response;
    }


    @GetMapping("/export")
    @ApiOperation("批量导出")
    @SOLog(tableName = "mgt_customer",remark = "客户管理导出" )
    public Response export(CustomerVo customerVo, HttpServletResponse response){
        customerService.export(customerVo,response);
        return Response.success();
    }

    @GetMapping("/exportError")
    @ApiOperation("批量导出异常信息")
    @SOLog(tableName = "mgt_customer",remark = "客户管理异常信息导出" )
    public Response exportError(CustomerVo customerVo, String key,HttpServletResponse response){
        customerService.exportError(key,response);
        return Response.success();
    }

    /**
     * 根据客户编码、租户ID 查询客户信息
     * @param customerCode
     * @return
     */
    @PostMapping(value="/selectByCustomerCode",produces = "application/json;charset=UTF-8")
    @ApiOperation("根据客户编码查询客户信息")
    public Response<CustomerTenantVo> selectByCustomerCode(@RequestBody String customerCode){
        CustomerTenantVo customerTenantVo = customerTenantService.selectByCustomerCode(customerCode);
        return Response.successWithResult(customerTenantVo);
    }

}
