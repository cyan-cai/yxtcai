package com.java.yxt.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 【短数据】下行数据：计费标识变化响应请求DTO
 *
 * @author Ailos
 * @version 1.0
 * @date 2020/8/29 18:48
 */
@Data
@ApiModel
@Slf4j
public class ShortDataChargingStatusResponseDto  {

    @NotNull(message = "requestId")
    @ApiModelProperty(value = "请求ID", required = true)
    private String requestId;

    @NotNull(message = "serverId不能为空")
    @ApiModelProperty(value = "服务ID", required = true)
    private Short serverId;

    @NotBlank(message = "msisdn不能为空")
    @ApiModelProperty(value = "终端号码", required = true)
    private String msisdn;

    @NotNull(message = "messageSn不能为空")
    @ApiModelProperty(value = "消息序号", required = true)
    private Integer messageSn;

    @NotNull(message = "result不能为空")
    @ApiModelProperty(value = "计费标识响应结果", required = true)
    private Integer result;

    @NotNull(message = "chargeIdentity不能为空")
    @ApiModelProperty(value = "新的终端计费标识", required = true)
    private Byte chargeIdentity;

    @Override
    public String toString(){
        try {
            return JsonUtil.objectToString(this,this.getClass().getName());
        } catch (JsonProcessingException e) {
            log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
        }
            return null;
    }
}
