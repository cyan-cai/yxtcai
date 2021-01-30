package com.java.yxt.vo;

import com.java.yxt.po.ProductionPo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ProductionVo
 *
 * @author zanglei
 * @version V1.0
 * @description 销售品vo
 * @Package com.java.yxt.vo
 * @date 2020/9/10
 */
@Data
public class ProductionVo extends ProductionPo implements Serializable {

    private static final long serialVersionUID = 2L;

    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页条数
     */
    private Integer size;
    /**
     * 创建时间范围查询
     */
    private List<String> createTimeList;
    /**
     * 创建开始时间
     */
    private String createBeginTime;
    /**
     * 创建结束时间
     */
    private String createEndTime;
    /**
     * api服务类型
     */
    private String apiCategory;
    /**
     * api名称
     */
    private String apiName;
}
