package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.ApiStrategyPo;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 策略po
 * @author zanglei
 */
@Data
@Slf4j
public class ApiStrategyVo extends ApiStrategyPo implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * 外部地址
     */
    private String extroPath;
    /**
     * 内部地址
     */
    private String internalPath;

    /**
     * api 名称
     */
    private String apiName;

    /**
     * api目录名称
     */
    private String apiCatalogName;

    /**
     * api 服务类型
     */
    private String apiCategory;

    /**
     * 开始页
     */
    private Integer current;

    /**
     * 每页显示条数
     */
    private Integer size;

    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getName());
        } catch (JsonProcessingException e) {
            log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
        }
        return  null;
    }
}