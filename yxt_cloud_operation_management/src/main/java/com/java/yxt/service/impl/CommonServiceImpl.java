package com.java.yxt.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.java.yxt.logger.feign.entity.Admin;
import com.java.yxt.service.ICommonService;
import com.java.yxt.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: 公共方法实现类
 * @author 李峰
 * @date 2020年11月17日 下午3:54:35
 */
@Slf4j
@Service
public class CommonServiceImpl implements ICommonService {

	@Autowired
	RedissonClient redisson;

	@Override
	public Admin getUserInfo(HttpServletRequest request) {
		String token = request.getHeader("access-token");
//      token ="DbNkWjHOQdoFtqRJzrIYWeHPbSPWq/xVKZ4VO4t4eyruy6S6Nv1AYg==";
		if (StringUtil.isNotBlank(token)) {
			RBucket<String> bucket = redisson.getBucket(token);
			Admin admin = JSONObject.parseObject(bucket.get(), Admin.class);
			return admin;
		}
		return null;
	}

}
