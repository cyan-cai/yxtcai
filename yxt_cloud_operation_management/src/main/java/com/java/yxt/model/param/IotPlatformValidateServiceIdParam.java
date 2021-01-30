package com.java.yxt.model.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: 平台管理PO
 * @author 李峰
 * @date 2020年11月12日 下午4:10:41
 */
@ApiModel
@Data
@Slf4j
public class IotPlatformValidateServiceIdParam {
	@ApiModelProperty(value = "主键ID", required = false)
	private Long id;
	@ApiModelProperty(value = "serverId，卫星公司定义", required = true, example = "S00001")
	@TableField
	private String serverId;

	@Override
	public String toString() {
		try {
			return JsonUtil.objectToString(this, this.getClass().getName());
		} catch (JsonProcessingException e) {
			log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
		}
		return null;
	}
}
