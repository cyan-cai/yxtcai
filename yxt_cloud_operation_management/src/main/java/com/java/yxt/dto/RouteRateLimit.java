package com.java.yxt.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @Description:
 * @author 李峰
 * @date 2020年8月29日 下午4:54:10
 */
@Data
public class RouteRateLimit implements Serializable {

	private static final long serialVersionUID = 1L;


	private String id;
	/**
	 * api Id
	 */
	private String apiId;
	/**
	 * 外部地址
	 */
	private String extroPath;
	/**
	 * 内部地址
	 */
	private String internalPath;
	/**
	 * 每日限流
	 */
	private int perDayLimit;
	/**
	 * 每月限流
	 */
	private int perMonthLimit;
	/**
	 * 每秒限流
	 */
	private int perSecondLimit;
	/**
	 * 允许访问开始时间 HH:mm:ss
	 */
	private String perDayFrom;
	/**
	 * 允许访问结束时间 HH:mm:ss
	 */
	private String perDayTo;
}
