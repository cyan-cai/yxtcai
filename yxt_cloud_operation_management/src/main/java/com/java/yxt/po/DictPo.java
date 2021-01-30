package com.java.yxt.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典表po
 * @author zanglei
 */
@Data
public class DictPo implements Serializable {
    /**
     *主键
     */
    private Long id;

    /**
     *
     * 类型
     */
    private String type;

    /**
     *
     * 代码
     */
    private Integer code;

    /**
     *
     * 描述
     */
    private String desc;

    /**
     *排序
     */
    private Integer sort;

    /**
     *
     * 备注
     */
    private String remark;


    private Integer createrId;


    private Date createTime;


    private Integer updaterId;


    private Date updateTime;


    private static final long serialVersionUID = 1L;

}