package com.java.yxt.feign;

import com.java.yxt.base.Response;
import com.java.yxt.dto.ChargeConfirmDto;
import com.java.yxt.dto.ShortDataChargingStatusNotifyDto;
import com.java.yxt.dto.ShortDataChargingStatusResponseDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * 调用协议网关服务
 */
@FeignClient(value = "tcpsvc",fallbackFactory = ProtocolFeignFallbackImpl.class)
@RequestMapping("/service/521/api/v1/sd")
public interface ProtocolFeignService {

    @ApiOperation(value="短数据计费标识响应接口", notes="短数据计费标识响应接口")
    @PostMapping(value = "/charging/res")
    Response chargingRes(@RequestBody @Valid ShortDataChargingStatusResponseDto dto);

    @ApiOperation(value="短数据计费标识通知接口", notes="短数据计费标识变化通知接口")
    @PostMapping(value = "/charging/notify")
    @Async
    Response chargingNotify(@RequestBody @Valid ShortDataChargingStatusNotifyDto dto) ;

    @ApiOperation(value="短数据终端停复机确认接口", notes="短数据终端停复机确认接口")
    @PostMapping(value = "/charge/confirm")
    Response chargeConfirm(@RequestBody ChargeConfirmDto dto);
}
