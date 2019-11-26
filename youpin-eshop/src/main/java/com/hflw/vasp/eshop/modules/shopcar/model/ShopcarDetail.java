package com.hflw.vasp.eshop.modules.shopcar.model;

import com.hflw.vasp.modules.entity.Shopcar;

import java.math.BigDecimal;

/**
 * 购物车表
 * 
 * @author liumh
 * @date 2019-04-02 16:00:37
 */
public class ShopcarDetail extends Shopcar {
	/**
	 * 商品全名称
	 */
	private String name;
	/**
	 * 到店价
	 */
	private BigDecimal shopPrice;

	/**
	 * 商品主图片
	 */
	private String picUrl;

	/**
	 * 成本价
	 */
	private BigDecimal costPrice;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getShopPrice() {
		return shopPrice;
	}

	public void setShopPrice(BigDecimal shopPrice) {
		this.shopPrice = shopPrice;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
}
