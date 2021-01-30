package com.java.yxt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.dto.CustomerDto;
import com.java.yxt.dto.TermIdDto;
import com.java.yxt.dto.TermIdRecDto;
import com.java.yxt.dto.TerminalCallbackDto;
import com.java.yxt.service.BusinessService;
import com.java.yxt.service.CustomerService;
import com.java.yxt.service.TerminalCallbackService;
import com.java.yxt.service.TerminalService;
import com.java.yxt.service.impl.FeignServiceImpl;
import com.java.yxt.util.ValidateUtil;
import com.java.yxt.vo.CustomerVo;
import com.java.yxt.vo.ServiceVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * FeignController
 *
 * @author zanglei
 * @version V1.0
 * @description 提供给各个微服务接口调用
 * @Package com.java.yxt.controller
 * @date 2020/9/16
 */
@RestController
@Slf4j
@RequestMapping("feign")
public class FeignController {

    @Autowired
    CustomerService customerService;

    @Autowired
    TerminalCallbackService terminalCallbackService;

    @Autowired
    BusinessService businessService;

    @Autowired
    TerminalService terminalService;

    @Autowired
    FeignServiceImpl feignService;



    /**
     * 根据终端号码分页查询
     * @param customerDto
     * @return
     */
    @PostMapping("/customer/selectbymsisdn")
    @ApiOperation("根据终端号码分页查询客户信息")
    public Response<Page<CustomerDto>> selectCustomer(@RequestBody CustomerDto customerDto){
        if(customerDto.getCurrent()==null && customerDto.getSize()==null){
            Page page=new Page();
            List<CustomerDto> list= customerService.selectByMsisdn(null,customerDto);
            page.setRecords(list);
            return Response.successWithResult(page);
        }
        Page<CustomerDto> page = new Page<>(customerDto.getCurrent(),customerDto.getSize());
        page.setRecords(customerService.selectByMsisdn(page,customerDto));
        return Response.successWithResult(page);
    }

    /**
     * 根据客户id、终端号码、服务类型 查询客户信息
     * @param customerDto
     * @return
     */
    @PostMapping("/customer/common/select")
    @ApiOperation("根据终端号码、服务类型分页查询客户信息")
    public Response<List<CustomerVo>> selectCommonCustomer(@RequestBody CustomerDto customerDto){
            List<CustomerVo> list = customerService.selectCommon(null,customerDto);
            return Response.successWithResult(list);
    }

    /**
     * 根据终端号码和服务类型查询客户信息
     * 服务类型：
     * 精定位(短信渠道、物联网渠道)--1
     * 粗定位(短信渠道、物联网渠道)--2
     * 物联网短数据--3
     * 物联网行业短信--4
     * 短报文广播--5
     * @param terminalCallbackDtos
     * @return
     */
    @PostMapping("/msisdn/apicategory")
    @ApiOperation("根据终端号码和服务类型查询客户信息")
    public Response getCustomerInfoByMsisdnCategory(@RequestBody List<TerminalCallbackDto> terminalCallbackDtos){
        return  Response.successWithResult(feignService.getCustomerInfoByMsisdnCategory(terminalCallbackDtos));
    }

    /**
     * 获取开通saas客户的回调地址
     * apiCategory--服务类型：
     * 精定位(短信渠道、物联网渠道)--1
     * 粗定位(短信渠道、物联网渠道)--2
     * 物联网短数据--3
     * 物联网行业短信--4
     * 短报文广播--5
     * isOpenSaas： 0未开通，1开通
     * @return
     */
    @PostMapping("/saas/customercallbackurl")
    @ApiOperation("获取开通saas客户的回调地址")
    public Response getSaasCustomerAndMsisdnApiCategory(@RequestBody  String apiCategory){
        return feignService.getSaasCustomerAndMsisdnApiCategory(apiCategory);
    }


    /**
     * 根据客户编码和服务类型获取回调地址
     * apiCategory--服务类型：
     * 精定位(短信渠道、物联网渠道)--1
     * 粗定位(短信渠道、物联网渠道)--2
     * 物联网短数据--3
     * 物联网行业短信--4
     * 短报文广播--5
     * @return
     */
    @PostMapping("/customercode/customercallbackurl")
    @ApiOperation("根据客户编码和服务类型获取回调地址")
    public Response getCallbackUrlByCusCodeApiCategory(@RequestBody Map<String,String> map){
        String customerCode = map.get("customerCode");
        String apiCategory  = map.get("apiCategory");
        if(ValidateUtil.isEmpty(customerCode) || ValidateUtil.isEmpty(apiCategory)){
            log.warn("根据客户编码和服务类型获取回调地址参数为空：{}，{}",customerCode,apiCategory);
            return Response.OptError();
        }
        return feignService.getCallbackUrlByCusCodeApiCategory(customerCode,apiCategory);
    }

    @PostMapping("/business/select")
    @ApiOperation("查询业务标识")
    public Response selectService(@RequestBody TerminalCallbackDto terminalCallbackServiceDto){
        ServiceVo serviceVo = new ServiceVo();
        serviceVo.setCustomerCode(terminalCallbackServiceDto.getMsisdn());
        serviceVo.setServiceCode(terminalCallbackServiceDto.getApiCategory());
        serviceVo.setCallbackUrl("http://oml:555/test");
       return Response.successWithResult(serviceVo);
    }

    @PostMapping("/terminalblack/select")
    @ApiOperation("查询客户对应终端号码通讯状态")
    public Response selectTermIds(@RequestBody TermIdDto termIdDto){
        Response response=feignService.selectTermIds(termIdDto);
        return response;
    }

    @PostMapping("/terminalblack/selectRec")
    @ApiOperation("查询终端号码对应客户通讯状态")
    public Response selectRecTermIds(@RequestBody TermIdRecDto termIdRecDto){
        Response response=feignService.selectRecTermIds(termIdRecDto);
        return response;
    }
}
