package com.java.yxt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.yxt.dao.TerminalDeviceAuditLogMapper;
import com.java.yxt.po.TerminalDeviceAuditLogPo;
import com.java.yxt.service.ITerminalDeviceAuditLogService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: 终端设备管理
 * @author 李峰
 * @date 2020年9月28日 下午3:12:19
 */
@Service
@Slf4j
public class TerminalDeviceAuditLogServiceImpl extends
		ServiceImpl<TerminalDeviceAuditLogMapper, TerminalDeviceAuditLogPo> implements ITerminalDeviceAuditLogService {

	@Autowired
	TerminalDeviceAuditLogMapper terminalDeviceAuditLogMapper;

}
