package com.java.yxt.vo;

import com.java.yxt.po.ApiRelationCatalogPo;
import lombok.Data;

import java.util.List;

/**
 * ApiRelationCatalogVo
 *
 * @author zanglei
 * @version V1.0
 * @description
 * @Package com.java.yxt.vo
 * @date 2020/9/11
 */
@Data
public class ApiRelationCatalogVo extends ApiRelationCatalogPo {

    /**
     * 目录要关联的apiId列表
     */
    private List<String> apiIds;
}
