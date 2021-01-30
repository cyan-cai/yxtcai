package com.java.yxt.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.java.yxt.base.Response;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.model.param.IotPlatformInsertParam;
import com.java.yxt.model.param.IotPlatformQueryParam;
import com.java.yxt.model.param.IotPlatformUpdateParam;
import com.java.yxt.model.param.IotPlatformValidateServiceIdParam;
import com.java.yxt.model.vo.IotPlatformVo;

/**
 * 
 * @Description: 平台管理
 * @author 李峰
 * @date 2020年11月12日 下午4:19:07
 */
public interface IotPlatformService {
	/**
	 * 添加平台
	 * 
	 * @param iotPlatformVo
	 * @return
	 */
	Response<?> insert(IotPlatformInsertParam iotPlatformVo);

	/**
	 * 修改平台信息
	 * 
	 * @param iotPlatformVo
	 * @return
	 */
	Response<?> update(IotPlatformUpdateParam iotPlatformVo);

	/**
	 * 查询单个平台信息
	 * 
	 * @param id
	 * @return
	 */
	Response<?> info(Long id);

	/**
	 * 删除平台信息
	 * 
	 * @param idList
	 * @return
	 */
	Response<?> delete(List<Long> idList);

	/**
	 * 分页查询
	 * 
	 * @param sensitiveWordQueryVO
	 * @return
	 */
	Response<IPage<IotPlatformVo>> selectByPage(IotPlatformQueryParam iotPlatformQueryParam);


	/**
	 * 校验平台标识
	 * 
	 * @param validateParam
	 * @return
	 */
	Response<?> validateServerId(IotPlatformValidateServiceIdParam validateParam);
}
