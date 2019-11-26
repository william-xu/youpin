package com.hflw.vasp.eshop.modules.order.model;

import com.hflw.vasp.modules.entity.StoreOrder;

import java.util.List;

/**
 * 订单商品关系表
 *
 * @author liumh
 * @date 2019-04-09 15:03:07
 */
public class StoreOrderGoodsModel {

    private StoreOrder storeOrder;

    private List<OrderGoodsDetailModel> orderGoodsDetailModels;


    public StoreOrder getStoreOrder() {
        return storeOrder;
    }

    public void setStoreOrder(StoreOrder storeOrder) {
        this.storeOrder = storeOrder;
    }

    public List<OrderGoodsDetailModel> getOrderGoodsDetailModels() {
        return orderGoodsDetailModels;
    }

    public void setOrderGoodsDetailModels(List<OrderGoodsDetailModel> orderGoodsDetailModels) {
        this.orderGoodsDetailModels = orderGoodsDetailModels;
    }
}
