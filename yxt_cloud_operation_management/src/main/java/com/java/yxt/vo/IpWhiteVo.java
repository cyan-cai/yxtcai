package com.java.yxt.vo;

import com.java.yxt.po.IpWhitePo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zanglei
 * @version V1.0
 * @description ip白名单
 * @Package com.java.yxt.vo
 * @date 2020/9/24
 */
@Data
public class IpWhiteVo extends IpWhitePo implements Serializable {

    private static final long serialVersionUID = 2L;


    /**
     * ip列表
     */
    private List<IPVo> ips;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 当前页
     */
    private Integer current;
    /**
     * 条数
     */
    private Integer size;
    /**
     * 开始时间，结束时间
     */
    private List<String> createTimeList;
    /**
     * 创建开始时间
     */
    private String createBeginTime;
    /**
     * 创建结束时间
     */
    private String createEndTime;
    /**
     * 白名单状态字符串
     */
    private String statusStr;

}
