package com.java.yxt.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @Description: page入参封装
 * @author 李峰
 * @date 2020年9月11日 下午4:47:06
 */
public class PageParam {
	/**
	 * 当前页面
	 */
	@ApiModelProperty(value = "当前页码", required = false, example = "1")
	private int page = 1;
	/**
	 * 每页条数
	 */
	@ApiModelProperty(value = "每页条数", required = false, example = "10")
	private int size = 10;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
