package com.java.yxt.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.yxt.base.Response;
import com.java.yxt.dao.TerminalDeviceAuditLogMapper;
import com.java.yxt.dao.TerminalDeviceMapper2;
import com.java.yxt.po.TerminalDeviceAuditLogPo;
import com.java.yxt.po.TerminalDevicePo2;
import com.java.yxt.service.ITerminalDeviceService;
import com.java.yxt.vo.TermainDeviceQueryVo;
import com.java.yxt.vo.TerminalDeviceVo2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @Description: 终端设备管理
 * @author 李峰
 * @date 2020年9月28日 下午3:12:19
 */
@Service
@Slf4j
public class TerminalDeviceServiceImpl2 extends ServiceImpl<TerminalDeviceMapper2, TerminalDevicePo2>
		implements ITerminalDeviceService {

	@Autowired
	TerminalDeviceMapper2 terminalDeviceMapperMapper;
	@Autowired
	TerminalDeviceAuditLogMapper terminalDeviceAuditLogMapper;

	@Override
	public Response insert(TerminalDevicePo2 terminalDevicePo) {
		int ret = terminalDeviceMapperMapper.insert(terminalDevicePo);
		if (ret > 0) {
			return Response.success();
		} else {
			log.error("新增终端设备数据库失败");
			return Response.dbOptError();
		}

	}

	@Override
	public Response update(TerminalDevicePo2 terminalDevicePo) {
		int ret = terminalDeviceMapperMapper.updateById(terminalDevicePo);
		if (ret > 0) {
			return Response.success();
		} else {
			log.error("新增终端设备数据库失败");
			return Response.dbOptError();
		}
	}

	@Override
	public Response<Page<TerminalDeviceVo2>> selectByPage(TermainDeviceQueryVo termainDeviceQueryVo) {
		Page<TerminalDeviceVo2> page = new Page<>(termainDeviceQueryVo.getPage(), termainDeviceQueryVo.getSize());
		List<TerminalDeviceVo2> records = terminalDeviceMapperMapper.selectTerminalDeviceList(page,
				termainDeviceQueryVo);
		page.setRecords(records);
		return Response.successWithResult(page);
	}

	@Override
	public Response<?> delete(List<Long> idList) {
		int ret = terminalDeviceMapperMapper.deleteBatchIds(idList);
		if (ret > 0) {
			return Response.success();
		} else {
			log.error("删除终端设备数据库失败");
			return Response.dbOptError();
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Response<?> pass(List<Long> idList) {
		for (Long id : idList) {
			TerminalDevicePo2 entity = new TerminalDevicePo2();
			entity.setId(id);
			entity.setStatus(3);
			terminalDeviceMapperMapper.updateById(entity);
			TerminalDeviceAuditLogPo terminalDeviceAuditLogPo = new TerminalDeviceAuditLogPo();
			terminalDeviceAuditLogPo.setTerminalDeviceId(id);
			terminalDeviceAuditLogPo.setAuditStatus(3);
			terminalDeviceAuditLogMapper.insert(terminalDeviceAuditLogPo);
		}
		return Response.success();
	}

	@Override
	public Response<?> reject(List<Long> idList) {
		for (Long id : idList) {
			TerminalDevicePo2 entity = new TerminalDevicePo2();
			entity.setId(id);
			entity.setStatus(2);
			terminalDeviceMapperMapper.updateById(entity);
			TerminalDeviceAuditLogPo terminalDeviceAuditLogPo = new TerminalDeviceAuditLogPo();
			terminalDeviceAuditLogPo.setTerminalDeviceId(id);
			terminalDeviceAuditLogPo.setAuditStatus(2);
			terminalDeviceAuditLogMapper.insert(terminalDeviceAuditLogPo);
		}
		return Response.success();
	}



}
