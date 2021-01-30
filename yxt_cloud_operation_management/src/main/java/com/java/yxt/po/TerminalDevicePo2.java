package com.java.yxt.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @Description: 终端设备po
 * @author 李峰
 * @date 2020年9月29日 下午3:59:30
 */
@Data
@TableName("mgt_terminal_device")
public class TerminalDevicePo2 implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "终端设备ID", required = false, accessMode = AccessMode.READ_ONLY)
	@TableId(type = IdType.AUTO)
	private Long id;
	@ApiModelProperty(value = "imei", required = true, example = "6556565656565")
	@TableField
	private String imei;
	@ApiModelProperty(value = "终端类型", required = true, example = "1")
	@TableField
	private Integer type;
	@ApiModelProperty(value = "终端厂商", required = true, example = "华为")
	@TableField
	private String company;
	@ApiModelProperty(value = "终端型号", required = true, example = "P40")
	@TableField
	private String model;
	@ApiModelProperty(value = "meid", required = true, example = "456456")
	@TableField
	private String meid;
	@ApiModelProperty(value = "设备状态，1.待提交 2.待审核  3.审核驳回 4.审核通过", required = true, example = "1")
	@TableField
	private Integer status;
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
