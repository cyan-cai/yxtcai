package com.java.yxt.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.java.yxt.po.ProductionApiRelationPo;
import com.java.yxt.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * ProductionApiRelationVo
 *
 * @author zanglei
 * @version V1.0
 * @description 销售品和api关系Vo
 * @Package com.java.yxt.vo
 * @date 2020/9/10
 */
@Data
@Slf4j
public class ProductionApiRelationVo extends ProductionApiRelationPo {

	/**
	 * api名称
	 */
	private String name;
	/**
	 * api服务类型
	 */
	private String apiCategory;

	/**
	 * apiid集合
	 */
	private List<String> apiIds;
	/**
	 * 销售品编码
	 */
	private String saleCode;
	/**
	 * 起始页
	 */
	private Integer current;
	/**
	 * 每页显示条数
	 */
	private Integer size;
	/**
	 * 状态  0失效 1有效
	 */
	private Integer status;
	/**
	 * api状态
	 */
	private Integer apiStatus;
	/**
	 * 销售品状态
	 */
	private Integer productStatus;

	/**
	 * 销售品名称
	 */
	private String productName;

	/**
	 * 位置渠道 1 短信   2 短数据  3 粗定位
	 */
	private Integer channel;


	@Override
	public String toString() {
		try {
			return JsonUtil.objectToString(this, this.getClass().getName());
		} catch (JsonProcessingException e) {
			log.error("{} object转jsonString异常：",this.getClass().getSimpleName(),e);
		}
		return null;
	}

}
