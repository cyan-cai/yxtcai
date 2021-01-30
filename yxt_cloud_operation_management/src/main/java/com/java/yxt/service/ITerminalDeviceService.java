package com.java.yxt.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.base.Response;
import com.java.yxt.po.TerminalDevicePo2;
import com.java.yxt.vo.TermainDeviceQueryVo;
import com.java.yxt.vo.TerminalDeviceVo2;

/**
 * 
 * @Description: 终端设备
 * @author 李峰
 * @date 2020年9月28日 下午3:08:37
 */
public interface ITerminalDeviceService {
	/**
	 * 添加终端设备
	 * 
	 * @param terminalDevicePo
	 * @return
	 */
	Response insert(TerminalDevicePo2 terminalDevicePo);

	/**
	 * 更新终端设备信息
	 * 
	 * @param terminalDevicePo
	 * @return
	 */
	Response update(TerminalDevicePo2 terminalDevicePo);

	/**
	 * 分页查询终端设备
	 * 
	 * @param termainDeviceQueryVo
	 * @return
	 */
	Response<Page<TerminalDeviceVo2>> selectByPage(TermainDeviceQueryVo termainDeviceQueryVo);

	/**
	 * 删除终端设备
	 * 
	 * @param idList
	 * @return
	 */
	Response<?> delete(List<Long> idList);

	/**
	 * 批量通过
	 * 
	 * @param idList
	 * @return
	 */
	Response<?> pass(List<Long> idList);

	/**
	 * 批量拒绝
	 * 
	 * @param idList
	 * @return
	 */
	Response<?> reject(List<Long> idList);

}
