package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.TerminalWhitePo;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * TerminalWhiteVo
 *
 * @author zanglei
 * @version V1.0
 * @description 终端白名单vo
 * @Package com.java.yxt.vo
 * @date 2020/9/19
 */
@Data
@Slf4j
public class TerminalWhiteVo extends TerminalWhitePo implements Serializable {
    private static final long serialVersionUID = 2L;
    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页条数
     */
    private Integer size;

    /**
     * 业务标识列表
     */
    private List<String> serviceCodes;
    /**
     * 客户名称
     */
    private String customerName;


    /**
     *终端号码
     */
    private String msisdn;

    /**
     * 终端类型
     */
    private Integer  type;

    /**
     * 终端类型字符串
     */
    private String  typeStr;
    /**
     * 终端用户状态
     */
    private Integer userStatus;
    /**
     * 终端用户状态字符串
     */
    private String userStatusStr;
    /**
     * 终端白名單状态字符串
     */
    private String statusStr;
    /**
     * 客户id
     */
    private String customerId;

    /**
     * 客户代码
     */
    private String customerCode;
    /**
     * 创建时间范围查询
     */
    private List<String> bindTimeList;
    /**
     * 创建开始时间
     */
    private String createBeginTime;
    /**
     * 创建结束时间
     */
    private String createEndTime;

    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getName());
        } catch (JsonProcessingException e) {
            log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
        }
        return null;
    }
}
