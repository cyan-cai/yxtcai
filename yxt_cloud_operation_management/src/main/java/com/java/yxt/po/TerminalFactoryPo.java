package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * 终端厂商Po
 * @author 蔡家明
 */
@Data
@Slf4j
public class TerminalFactoryPo extends  BasePo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *主键
     */
    private String id;

    /**
     * 厂商名称
     */
    private String factoryName;

    /**
     * 法人名称
     */
    private String corporationName;

    /**
     * 厂商电话
     */
        private String factoryPhoneNum;

    /**
     * 厂商地址
     */
    private String factoryAddress;

    /**
     * 厂商状态
     */
    private Integer factoryStatus;

    /**
     *创建时间
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
