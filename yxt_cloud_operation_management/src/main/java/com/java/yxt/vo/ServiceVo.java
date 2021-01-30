package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.ProductionPo;
import com.java.yxt.po.ServicePo;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * ServiceVo
 *
 * @author zanglei
 * @version V1.0
 * @description 业务标识vo
 * @Package com.java.yxt.vo
 * @date 2020/9/16
 */
@Data
@Slf4j
public class ServiceVo extends ServicePo implements Serializable {

    private static final long serialVersionUID = 2L;
    /**
     * 终端id
     */
    private String terminalId;
    /**
     * 起始页
     */
    private Integer current;
    /**
     * 每页条数
     */
    private Integer size;

    /**
     * 业务标识list
     */
    private List<String> serviceCods;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 销售品名称
     */
    private String productionName;
    /**
     * 销售品信息
     */
    private List<ProductionPo> productionPos;
    /**
     * 客户是否开通Saas 1开通 0不开通
     */
    private  Integer saas;

    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this,this.getClass().getSimpleName());
        } catch (JsonProcessingException e) {
            log.error("{}对象转jsonString失败",this.getClass().getSimpleName());
        }
        return null;
    }
}
