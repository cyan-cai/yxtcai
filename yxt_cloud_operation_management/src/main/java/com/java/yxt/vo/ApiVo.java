package com.java.yxt.vo;

import com.java.yxt.po.ApiPo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ApiVo
 *
 * @author zanglei
 * @version V1.0
 * @description apivo
 * @Package com.java.yxt.vo
 * @date 2020/9/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApiVo extends ApiPo {

    /**
     * api目录id
     */
    private String apiCatalogId;

    /**
     * 销售品id
     */
    private String productId;
    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页显示条数
     */
    private Integer size;

    /**
     * api目录名称
     */
    private String catalogName;
    /**
     * api父目录名称
     */
    private String parentCatalogName;
    /**
     * 上级目录id
     */
    private String parentCatalogId;
    /**
     * 状态 0 无效 1 有效
     */
    private Integer status;
}
