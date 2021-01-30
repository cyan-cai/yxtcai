package com.java.yxt.model.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.java.yxt.model.po.IotPlatformPo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 
 * @Description: 平台管理vo
 * @author 李峰
 * @date 2020年11月12日 下午4:09:21
 */
@Data
@ApiModel
@TableName("mgt_iot_platform")
public class IotPlatformVo extends IotPlatformPo {
}
