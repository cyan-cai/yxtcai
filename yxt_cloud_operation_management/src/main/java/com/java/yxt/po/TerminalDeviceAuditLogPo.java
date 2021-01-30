package com.java.yxt.po;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;

/**
 * 
 * @Description: 终端设备日志po
 * @author 李峰
 * @date 2020年9月29日 下午4:00:20
 */
@Data
@TableName("mgt_terminal_device_audit_log")
public class TerminalDeviceAuditLogPo implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "审核记录ID", required = false, accessMode = AccessMode.READ_ONLY)
	@TableId(type = IdType.AUTO)
	private Long id;
	@ApiModelProperty(value = "终端设备id", required = true, example = "1")
	@TableField
	private Long terminalDeviceId;
	@ApiModelProperty(value = "审核状态，1.待提交 2.待审核  3.审核驳回 4.审核通过", required = true, example = "1")
	@TableField
	private Integer auditStatus;
	@ApiModelProperty(value = "审核人id", required = true, example = "1")
	@TableField
	private Long auditerId;
	@ApiModelProperty(value = "审核时间", required = true, example = "2020-09-14 15:42:56")
	@TableField
	private Date auditTime;
	@ApiModelProperty(value = "租户ID", required = true, example = "2")
	@TableField
	private Long tenantId;
	@ApiModelProperty(value = "创建人ID", required = true, example = "1")
	@TableField
	private Long createrId;
	@ApiModelProperty(value = "创建时间", required = false, example = "2020-09-14 15:42:56")
	@TableField
	private Date createTime;
	@ApiModelProperty(value = "更新人ID", required = false, example = "2")
	@TableField
	private Long updaterId;
	@ApiModelProperty(value = "更新时间", required = false, example = "2020-09-14 15:42:56")
	@TableField
	private Date updateTime;

}
