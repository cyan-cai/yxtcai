package com.java.yxt.dto;

import com.java.yxt.vo.CustomerVo;
import lombok.Data;

import java.util.List;

/**
 * CustomerDto
 *
 * @author zanglei
 * @version V1.0
 * @description 客户dto，供外部调用使用
 * @Package com.java.yxt.dto
 * @date 2020/9/16
 */
@Data
public class CustomerDto extends CustomerVo {



    private String imsi;
    /**
     * 客户id
     */
    private String customerId;
    /**
     *终端号码列表
     */
    private List<String> msisdnList;
    /**
     * 终端类型名称
     */
    private String typeName;
    /**
     * 业务标识id列表
     */
    private List<String> serviceIds;
}
