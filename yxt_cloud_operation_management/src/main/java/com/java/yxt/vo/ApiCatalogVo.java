package com.java.yxt.vo;

import com.java.yxt.po.ApiCatalogPo;
import lombok.Data;

import java.util.List;

/**
 * ApiCatalogVo
 *
 * @author zanglei
 * @version V1.0
 * @description api目录Vo
 * @Package com.java.yxt.vo
 * @date 2020/9/11
 */
@Data
public class ApiCatalogVo extends ApiCatalogPo {
    /**
     * api目录关联的api数量
     */
    private Integer apiCount;
    /**
     * 子目录列表
     */
    private List<ApiCatalogVo> childList;
    /**
     * 当前页
     */
    private Integer current;
    /**
     * 条数
     */
    private Integer size;
}
