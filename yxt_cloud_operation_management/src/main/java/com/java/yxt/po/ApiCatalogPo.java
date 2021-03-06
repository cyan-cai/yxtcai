package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * ApiCatalogPo
 *
 * @author zanglei
 * @version V1.0
 * @description api目录表对应po
 * @Package com.java.yxt.po
 * @date 2020/9/11
 */
@Data
@Slf4j
public class ApiCatalogPo extends  BasePo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;
    /**
     * 父目录名称
     */
    private String parentName;
    /**
     * 父级id
     */
    private String parentId;
    /**
     * 展示顺序
     */
    private Integer seqNum;

    /**
     * 目录名称
     */
    private String name;

    /**
     *'目录描述'
     */
    private String description ;
    /**
     * 目录级别
     */
    private Integer  level;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 更新人ID
     */
    private String updaterId ;
    /**
     * 创建时间
     */
    private Date createTime ;
    /**
     * 更新时间
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
