package com.java.yxt.service;

import javax.servlet.http.HttpServletRequest;

import com.java.yxt.logger.feign.entity.Admin;

/**
 * 
 * @Description: 公共方法接口
 * @author 李峰
 * @date 2020年11月17日 下午3:54:19
 */
public interface ICommonService {
	/**
	 * 从redis获取用户信息
	 * 
	 * @return
	 */
	Admin getUserInfo(HttpServletRequest request);
}
