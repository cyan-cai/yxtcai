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
public class SecretKeyHistoryPo extends  BasePo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *主键
     */
    private String id;

    /**
     * 密钥版本
     */
    private String keyVersion;

    /**
     * 密钥ID
     */
    private String keyId;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 有效开始时间
     */
    private Date keyStartTime;

    /**
     * 有效开始时间
     */
    private Date keyEndTime;


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
