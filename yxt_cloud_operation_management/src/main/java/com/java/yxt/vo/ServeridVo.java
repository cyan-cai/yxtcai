package com.java.yxt.vo;

import com.java.yxt.po.ServeridPo;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * serverId管理表
 * </p>
 *
 * @author zanglei
 * @date 2020-09-25
 */
@Data
public class ServeridVo extends ServeridPo implements Serializable {

    private static final long serialVersionUID = 2L;
    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页条数
     */
    private Integer size;
}
