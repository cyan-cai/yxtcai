package com.java.yxt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java.yxt.model.param.IotPlatformQueryParam;
import com.java.yxt.model.po.IotPlatformPo;
import com.java.yxt.model.vo.IotPlatformVo;

/**
 * api目录接口
 * 
 * @author zanglei
 */
@Mapper
public interface IotPlatformMapper extends BaseMapper<IotPlatformPo> {
	/**
	 * 根据条件查询列表
	 * 
	 * @param page
	 * @param iotPlatformQueryParam
	 * @return
	 */
	List<IotPlatformVo> selectIotPlatformList(IPage<IotPlatformVo> page, @Param("param") IotPlatformQueryParam iotPlatformQueryParam);
}