package com.java.yxt.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.java.yxt.po.TerminalDeviceAuditLogPo;

/**
 * 
 * @Description: 终端设备审核日志dao
 * @author 李峰
 * @date 2020年9月28日 下午4:26:17
 */
@Mapper
public interface TerminalDeviceAuditLogMapper extends BaseMapper<TerminalDeviceAuditLogPo> {

}