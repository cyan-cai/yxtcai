package com.java.yxt.feign;

import com.java.yxt.base.Response;
import com.java.yxt.dto.ChargeConfirmDto;
import com.java.yxt.dto.ShortDataChargingStatusNotifyDto;
import com.java.yxt.dto.ShortDataChargingStatusResponseDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zanglei
 * @version V1.0
 * @description 协议网关回调
 * @Package com.java.yxt.feign
 * @date 2021/1/5
 */
@Component
@Slf4j
public class ProtocolFeignFallbackImpl implements FallbackFactory<ProtocolFeignService> {
    @Override
    public ProtocolFeignService create(Throwable throwable) {
        return new ProtocolFeignService() {
            @Override
            public Response chargingRes(ShortDataChargingStatusResponseDto dto) {
                log.error("调用计费标识响应异常：;\n 入参：{}",throwable,dto.toString());
                return null;
            }

            @Override
            public Response chargingNotify(ShortDataChargingStatusNotifyDto dto) {
                log.error("调用计费标识通知异常：;\n 入参：{}",throwable,dto.toString());
                return null;
            }

            @Override
            public Response chargeConfirm(ChargeConfirmDto dto) {
                log.error("调用短数据终端停复机确认异常：;\n 入参：{}",throwable,dto.toString());
                return null;
            }
        };
    }
}
