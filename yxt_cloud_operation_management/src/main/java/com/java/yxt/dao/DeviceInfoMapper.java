package com.java.yxt.dao;

import com.java.yxt.po.DeviceInfoPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * deviceInfo dao 层接口
 * @author caijiaming
 */
@Mapper
public interface DeviceInfoMapper extends MyBaseMapper<DeviceInfoPo>{
    /**
     * 删除设备增量信息
     * @return
     */
    int delete();

    /**
     * 更新设备增量信息
     * @param deviceInfoPo
     * @return
     */
    int insert(DeviceInfoPo deviceInfoPo);

    /**
     * 查询设备增量信息
     * @return
     */
    List<DeviceInfoPo> selectAll();
}