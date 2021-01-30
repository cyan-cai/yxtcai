package com.java.yxt.po;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * BasePo
 *
 * @author zanglei
 * @version V1.0
 * @description 基础vo实体类，供其他类继承
 * @Package com.java.yxt.po
 * @date 2020/11/11
 */
@Data
public class BasePo implements Serializable {
    private static final long serialVersionUID = 4L;

    /**
     * 创建人
     */
    private String createrName;

    /**
     *创建人id
     */
    private String createrId ;

    /**
     * 修改人
     */
    private String updaterId;
    /**
     *
     */
    private String updaterName;

    /**
     * 租户ID
     */
    private String tenantId ;
    /**
     * 组织id
     */
    @TableField(exist = false)
    private String orgId;
    /**
     * token
     */
    @TableField(exist = false)
    private String token;

    /**
     *    创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime ;
}
