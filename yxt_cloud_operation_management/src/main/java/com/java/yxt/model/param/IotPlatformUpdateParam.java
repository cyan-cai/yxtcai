package com.java.yxt.model.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
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
public class IotPlatformUpdateParam {
	@ApiModelProperty(value = "主键ID", required = false, accessMode = AccessMode.READ_ONLY)
	private Long id;
	@ApiModelProperty(value = "serverId，卫星公司定义", required = true, example = "S00001")
	@TableField
	private String serverId;
	@ApiModelProperty(value = "服务类型（配置表code）", required = true, example = "1")
	@TableField
	private String apiCategory;
	@ApiModelProperty(value = "状态 0 无效 1 有效", required = false, example = "1")
	@TableField
	private Integer status;
	@ApiModelProperty(value = "租户id", required = false, example = "1")
	@TableField
	private Long tenantId;
	@ApiModelProperty(value = "修改人id", required = false)
	@TableField
	private Long updaterId;
	@ApiModelProperty(value = "修改人", required = false)
	@TableField
	private String updaterName;

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
