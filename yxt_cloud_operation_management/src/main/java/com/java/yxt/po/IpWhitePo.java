package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端用户白名单po
 * @author zanglei
 */
@Data
@Slf4j
public class IpWhitePo extends BasePo implements Serializable {
    /**
     *主键
     */
    private String id;

    /**
     *客户id
     */
    private String customerId;

    /**
     *
     * ip 协议 IPV4,IPV6
     */
    private String ipProtocal;

    /**
     *IP地址
     */
    private String ip;

    /**
     *
     * 状态
     */
    private Short status;
    
    /**
     *
     * 创建时间
     */
    private Date createTime;

    /**
     *
     * 更新人id
     */
    private String updaterId;

    /**
     *
    更新时间
     */
    private Date updateTime;


    private static final long serialVersionUID = 1L;

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