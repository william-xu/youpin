package com.hflw.vasp.eshop.modules.shopcar.model;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 购物车表
 * 
 * @author liumh
 * @date 2019-04-02 16:00:37
 */

public class ShopcarModel implements Serializable {

	private static final long serialVersionUID = 7230477939668640627L;
	/**
	 * 商品id
	 */
	@NotNull(message = "商品id不能为空")
	private Integer goodsId;
	/**
	 * 商品数量
	 */
	@NotNull(message = "商品数量必须在0到10之间")
	@Range(min = 1, max = 10, message = "商品数量必须在0到10之间")
	private Integer goodsNum;

	private String name;

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ShopcarModel{" +
				"goodsId=" + goodsId +
				", goodsNum=" + goodsNum +
				", name='" + name + '\'' +
				'}';
	}
}
