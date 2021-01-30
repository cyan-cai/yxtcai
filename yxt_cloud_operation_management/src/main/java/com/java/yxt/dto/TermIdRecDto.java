package com.java.yxt.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * TermIdDto
 *
 * @author caijaiming
 * @version V1.0
 * @description 终端dto，供外部调用使用
 * @Package com.java.yxt.dto
 * @date 2021/01/04
 */
@Data
public class TermIdRecDto implements Serializable {

    private static final long serialVersionUID = 2L;
    /**
     * 终端号码
     */
    private String msisdnTermId;

    /**
     * 特服号
     */
    private String spServiceNumber;

    /**
     *客户代码
     */
    private String customerCode;

    /**
     * 终端号码[已验证通过]
     */
    private String srcRealMsisdn;

    /**
     * 终端号码[验证未通过]
     */
    private String srcBlackMsisdn;

    /**
     * 短信息接受方号码[已验证通过]
     */
    private String destRealServiceNumber;

    /**
     * 短信息接受方号码[验证未通过]
     */
    private String destBlackServiceNumber;

    /**
     * 0＝MO消息（终端发给SP） 6＝MT消息（SP发给终端，包括WEB上发送的点对点短消息）
     */
    private int msgType;
    /**
     * 0=失效  1=生效
     */
    private int status;


}

