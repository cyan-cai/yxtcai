package com.java.yxt.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.java.yxt.base.Response;
import com.java.yxt.base.ValidationEnum;
import com.java.yxt.dao.IotPlatformMapper;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.model.param.IotPlatformInsertParam;
import com.java.yxt.model.param.IotPlatformQueryParam;
import com.java.yxt.model.param.IotPlatformUpdateParam;
import com.java.yxt.model.param.IotPlatformValidateServiceIdParam;
import com.java.yxt.model.po.IotPlatformPo;
import com.java.yxt.model.vo.IotPlatformVo;
import com.java.yxt.service.IotPlatformService;
import com.java.yxt.util.StringUtil;
import com.java.yxt.util.ValidateUtil;

/**
 * 
 * @Description: 平台管理接口实现类
 * @author 李峰
 * @date 2020年11月12日 下午4:19:52
 */
@Service
public class IotPlatformImpl extends ServiceImpl<IotPlatformMapper, IotPlatformPo> implements IotPlatformService {
	@Autowired
	IotPlatformMapper iotPlatformMapper;
	@Autowired
	RedissonClient redisson;

	@Override
	public Response<?> insert(IotPlatformInsertParam iotPlatformVo) {
		// 校验
		if (StringUtil.isBlank(iotPlatformVo.getApiCategory())) {
			return new Response<>(ValidationEnum.MGT_OPT_PLATFORM_API_CATEGORY_IS_NULL);
		}
		if (StringUtil.isBlank(iotPlatformVo.getServerId())) {
			return new Response<>(ValidationEnum.MGT_OPT_PLATFORM_SERVER_ID_IS_NULL);
		}
		IotPlatformPo iotPlatformPo = new IotPlatformPo();
		BeanUtils.copyProperties(iotPlatformVo, iotPlatformPo);
		int ret = iotPlatformMapper.insert(iotPlatformPo);
		if (ret == 1) {
			return Response.success();
		} else {
			return Response.OptError();
		}

	}

	@Override
	public Response<?> update(IotPlatformUpdateParam iotPlatformVo) {
		if (StringUtil.isBlank(iotPlatformVo.getId() + "")) {
			return new Response<>(ValidationEnum.MGT_OPT_PLATFORM_ID_IS_NULL);
		}
		IotPlatformPo iotPlatformPo = new IotPlatformPo();
		BeanUtils.copyProperties(iotPlatformVo, iotPlatformPo);
		iotPlatformPo.setUpdateTime(new Date());
		int ret = iotPlatformMapper.updateById(iotPlatformPo);
		if (ret == 1) {
			return Response.success();
		} else {
			return Response.OptError();
		}
	}

	@Override
	public Response<?> info(Long id) {
		if (StringUtil.isBlank(id + "")) {
			return new Response<>(ValidationEnum.MGT_OPT_PLATFORM_ID_IS_NULL);
		}
		IotPlatformPo iotPlatformPo = iotPlatformMapper.selectById(id);
		IotPlatformVo iotPlatformVo = new IotPlatformVo();
		BeanUtils.copyProperties(iotPlatformPo, iotPlatformVo);
		return Response.successWithResult(iotPlatformVo);
	}

	@Override
	public Response<?> delete(List<Long> idList) {
		if (ValidateUtil.isEmpty(idList)) {
			return new Response<>(ValidationEnum.MGT_OPT_PLATFORM_DELETE_ID_IS_NULL);
		}
		idList.stream().forEach(id -> {
			IotPlatformPo entity = new IotPlatformPo();
			entity.setId(id);
			entity.setDeleteFlag(1);
			iotPlatformMapper.updateById(entity);
		});
		return Response.success();
	}

	@Override
	public Response<IPage<IotPlatformVo>> selectByPage(IotPlatformQueryParam iotPlatformQueryParam) {
		IPage<IotPlatformVo> page = new Page<>(iotPlatformQueryParam.getPage(), iotPlatformQueryParam.getSize());
		List<IotPlatformVo> records = iotPlatformMapper.selectIotPlatformList(page, iotPlatformQueryParam);
		page.setRecords(records);
		return Response.successWithResult(page);
	}


	@Override
	public Response<?> validateServerId(IotPlatformValidateServiceIdParam validateParam) {
		QueryWrapper<IotPlatformPo> queryWrapper = new QueryWrapper<IotPlatformPo>();
		// 未删除
		queryWrapper.eq("delete_flag", 0);
		if (validateParam.getId() != null) {
			// 修改时排除主键
			queryWrapper.ne("id", validateParam.getId());
		}
		if (StringUtil.isNotBlank(validateParam.getServerId())) {
			// 等于serviceId
			queryWrapper.eq("server_id", validateParam.getServerId());
		}
		Integer selectCount = iotPlatformMapper.selectCount(queryWrapper);
		if (selectCount > 0) {
			return new Response<>(ValidationEnum.IOT_PLATFORM_SERVICE_ID_EXISTS);
		}
		return Response.success();
	}

}
