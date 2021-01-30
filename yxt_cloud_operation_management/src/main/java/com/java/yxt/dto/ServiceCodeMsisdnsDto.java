package com.java.yxt.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * ServiceCodeMsisdnsDto
 *
 * @author zanglei
 * @version V1.0
 * @description 业务标识和终端msisdn对应关系
 * @Package com.java.yxt.dto
 * @date 2020/12/22
 */
@Data
public class ServiceCodeMsisdnsDto implements Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * 业务标识
     */
    private String serviceCode;
    /**
     * 生效时间
     */
    private Date startTime;
    /**
     *结束时间
     */
    private Date endTime;
    /**
     * 终端列表
     */
    private List<String> msisdnList;
}
