package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * ApiRelationCatalogPo
 *
 * @author zanglei
 * @version V1.0
 * @description api和目录关系Po
 * @Package com.java.yxt.po
 * @date 2020/9/11
 */
@Data
@Slf4j
public class ApiRelationCatalogPo extends BasePo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id ;
    /**
     * api id
     */
    private String apiId;
    /**
     * 目录id
     */
    private String apiCatalogId ;
    /**
     * 状态
     */
    private Integer status ;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人id
     */
    private String updaterId ;
    /**
     * 修改时间
     */
    private Date updateTime ;

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
