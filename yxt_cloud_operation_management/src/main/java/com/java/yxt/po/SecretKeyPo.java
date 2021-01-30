package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端设备Po
 * @author 蔡家明
 */
@Data
@Slf4j
public class SecretKeyPo extends  BasePo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *主键
     */
    private String id;

    /**
     * 密钥来源 1界面 2接口
     */
    private Integer keySource;

    /**
     * 密钥版本
     */
    private String keyVersion;

    /**
     * 客户代码
     */
    private String customerCode;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 密钥状态
     */
    private Integer keyStatus;

    /**
     * 有效开始时间
     */
    private Date keyStartTime;

    /**
     * 有效结束时间
     */
    private Date keyEndTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *更新时间
     */
    private Date updateTime;



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
