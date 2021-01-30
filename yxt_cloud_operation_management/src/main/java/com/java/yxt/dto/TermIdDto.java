package com.java.yxt.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

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
public class TermIdDto implements Serializable {

    private static final long serialVersionUID = 2L;
    /**
     * 特服号(sp)
     */
    private String spServiceNumber;

    /**
     *客户代码
     */
    private String customerCode;

    /**
     * 短信息接受方号码
     */
    List<String> msisdnTermIds;

    /**
     * 特服号(sp)[已验证通过]
     */
    private String srcRealServiceNumber;

    /**
     * 特服号(sp)[已验证通过]
     */
    private String srcBlackServiceNumber;

    /**
     * 短信息接受方号码[已验证通过]
     */
    List<String> destTermRealIds;

    /**
     * 短信息接受方号码[验证未通过]
     */
    List<String> destTermBlackIds;
    /**
     * 0＝MO消息（终端发给SP） 6＝MT消息（SP发给终端，包括WEB上发送的点对点短消息）
     */
    private int msgType;
    /**
     * 0=失效  1=生效
     */
    private  int status;
    /**
     * 优先级
     */
    private String priority;
}
