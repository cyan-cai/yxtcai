package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * Production
 *
 * @author zanglei
 * @version V1.0
 * @description 销售品po
 * @Package com.java.yxt.po
 * @date 2020/9/9
 */
@Data
@Slf4j
public class ProductionPo extends  BasePo implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;
    /**
     * 销售品名称
     */
    private String name;
    /**
     * 销售品编码
     */
    private String saleCode;
    /**
     * 销售品状态 0 有效 1 无效
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
     * 更新时间
     */
    private Date updateTime;
    /**
     * 生效时间
     */
    private Date effectiveStartTime;
    /**
     * 失效时间
     */
    private Date invalidStartTime;
    /**
     * 位置渠道 1 短信   2 短数据  3 粗定位
     */
    private Integer channel;

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
