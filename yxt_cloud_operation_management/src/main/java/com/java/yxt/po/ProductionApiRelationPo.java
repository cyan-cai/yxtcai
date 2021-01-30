package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * ProductionApiRelationPo
 *
 * @author zanglei
 * @version V1.0
 * @description 销售品api关系表
 * @Package com.java.yxt.po
 * @date 2020/9/10
 */
@Data
@Slf4j
public class ProductionApiRelationPo extends BasePo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private String id;
    /**
     * 销售品编码
     */
    private String productId;
    /**
     * apiId
     */
    private String apiId;
    /**
     * 状态 0有效,1无效
     */
    private Integer status;
    /**
     * 修改人id
     */
    private String updaterId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

    @Override
    public String toString() {
        try {
            return JsonUtil.objectToString(this, this.getClass().getName());
        } catch (JsonProcessingException e) {
            log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
        }
        return null;
    }

}
