package com.java.yxt.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @Description: redis Customer信息封装
 * @author 李峰
 * @date 2020年9月24日 下午3:56:39
 */
@Data
public class CustomerInfo implements Serializable {
	private static final long serialVersionUID = 2L;
	/**
	 * 客户标识
	 */
	public String customerCode;
	/**
	 * 客户状态
	 */
	public int status;
	/**
	 * 客户订购标识
	 */
	public List<String> serviceCode;
//	public List<ServiceVo> serviceCode;

}
