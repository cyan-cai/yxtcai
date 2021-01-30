package com.java.yxt.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 【短数据】下行数据：终端停复机确认DTO
 *
 * @author Ailos
 * @version 1.0
 * @date 2020/9/10
 */
@Data
@Slf4j
public class ChargeConfirmDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @NotNull(message = "requestId不能为空")
    @ApiModelProperty(value = "业务请求ID", required = true)
    private Long requestId;

    @ApiModelProperty(value = "服务ID")
    private Short serverId;

    @NotNull(message = "messageSn不能为空")
    @ApiModelProperty(value = "消息序号", required = true)
    private Integer messageSn;

    @NotBlank(message = "msisdn不能为空")
    @ApiModelProperty(value = "终端号", required = true)
    private String msisdn;

    @NotNull(message = "result不能为空")
    @ApiModelProperty(value = "确认结果", required = true)
    private Integer result;

    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getSimpleName());
        } catch (JsonProcessingException e) {
            log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
        }
        return null;
    }
}
