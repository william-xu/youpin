package com.hflw.vasp.eshop.modules.order.model;

import com.hflw.vasp.modules.entity.StoreOrder;

/**
 * 门店订单表
 * 
 * @author liumh
 * @date 2019-04-09 14:21:28
 */
public class StoreOrderModel extends StoreOrder {

	private  String picUrl;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
