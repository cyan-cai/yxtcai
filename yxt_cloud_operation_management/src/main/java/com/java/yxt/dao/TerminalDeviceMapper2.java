package com.java.yxt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.java.yxt.po.TerminalDevicePo2;
import com.java.yxt.vo.TermainDeviceQueryVo;
import com.java.yxt.vo.TerminalDeviceVo2;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 
 * @Description: 终端设备dao
 * @author 李峰
 * @date 2020年9月28日 下午4:26:17
 */
@Mapper
public interface TerminalDeviceMapper2 extends BaseMapper<TerminalDevicePo2> {
	/**
	 * 分页查询终端设备列表
	 * 
	 * @param page
	 * @param termainDeviceQueryVo
	 * @return
	 */
	List<TerminalDeviceVo2> selectTerminalDeviceList(Page<TerminalDeviceVo2> page,
													 TermainDeviceQueryVo termainDeviceQueryVo);
}