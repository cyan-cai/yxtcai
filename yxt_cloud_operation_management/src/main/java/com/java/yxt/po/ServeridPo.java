package com.java.yxt.po;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * serverId管理表
 * </p>
 *
 * @author zanglei
 * @date 2020-09-25
 */
@Data
@Slf4j
public class ServeridPo extends  BasePo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 平台标识
     */
    private String serverId;

    /**
     * 服务类型
     */
    private String apiCategory;

    /**
     * 0 有效 1 无效
     */
    private Integer status;

    /**
     * 更新人ID
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
            return JsonUtil.objectToString(this,this.getClass().getName());
        } catch (JsonProcessingException e) {
            log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
        }
        return null;
    }
}
