package com.java.yxt.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caijiaming
 * @version V1.0
 * @description 同步核心网信息实体类
 * @Package com.java.yxt.po
 * @date 2020/12/31
 */
@Data
public class DeviceInfoPo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *主键
     */
    private String id;

    /**
     *操作类型 1标识新增 2标识删除
     */
    private Integer operate;

    /**
     *国际移动设备识别码
     */
    private String imei;

    /**
     *描述信息
     */
    private String desc;

    @Override
    public String toString() {
        return operate+","+imei+","+desc;
    }
}
