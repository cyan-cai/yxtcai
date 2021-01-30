package com.java.yxt.vo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @Description: 终端设备查询条件vo
 * @author 李峰
 * @date 2020年9月29日 下午3:59:55
 */
@Data
public class TermainDeviceQueryVo extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "imei", required = true, example = "6556565656565")
	private String imei;
	@ApiModelProperty(value = "终端类型", required = true, example = "1")
	private Integer type;
	@ApiModelProperty(value = "终端厂商", required = true, example = "华为")
	private String company;

	@ApiModelProperty(value = "终端型号", required = true, example = "P40")
	private String model;
	@ApiModelProperty(value = "meid", required = true, example = "456456")
	private String meid;
	@ApiModelProperty(value = "设备状态，1.待提交 2.待审核  3.审核驳回 4.审核通过", required = true, example = "1")
	private Integer status;
	@ApiModelProperty(value = "创建人ID", required = true, example = "1")
	private Long creater;

	@ApiModelProperty(value = "开始创建时间", required = false, example = "2020-09-14 15:42:56")
	private Date createTimeStart;
	@ApiModelProperty(value = "结束创建时间", required = false, example = "2020-09-14 15:42:56")
	private Date createTimeEnd;

}
