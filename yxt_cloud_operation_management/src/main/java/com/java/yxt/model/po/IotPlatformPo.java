package com.java.yxt.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 
 * @Description: 平台管理PO
 * @author 李峰
 * @date 2020年11月12日 下午4:10:41
 */
@ApiModel
@Data
@Slf4j
@TableName("mgt_iot_platform")
public class IotPlatformPo {
	@ApiModelProperty(value = "主键ID", required = false, accessMode = AccessMode.READ_ONLY)
	@TableId(type = IdType.AUTO)
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
	@ApiModelProperty(value = "创建人id", required = false, example = "1")
	@TableField
	private Long createrId;
	@ApiModelProperty(value = "创建人", required = false, accessMode = AccessMode.READ_ONLY)
	@TableField
	private String createrName;
	@ApiModelProperty(value = "修改人", required = false, accessMode = AccessMode.READ_ONLY)
	@TableField
	private String updaterName;
	@ApiModelProperty(value = "创建时间", required = false, accessMode = AccessMode.READ_ONLY)
	@TableField
	private Date createTime;
	@ApiModelProperty(value = "修改人id", required = false)
	@TableField
	private Long updaterId;
	@ApiModelProperty(value = "修改时间", required = false)
	@TableField
	private Date updateTime;
	@ApiModelProperty(value = "是否删除 0未删除 1已删除", required = false)
	@TableField
	private int deleteFlag;

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
