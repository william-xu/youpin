package com.hflw.vasp.eshop.common.enums;

public interface OrderEnums {

    /**
     * 0.待付款：代表买家下单了但是还没有付款。
     * 1.待发货（同待接单）：代表买家付款了卖家还没有发货。
     * 2.已发货（同待收货）：代表卖家已经发货并寄出商品了。
     * 3.已完成（同待评价）：代表买家已经确认收到货了。
     * 4.已关闭（同已取消）：代表订单过期了买家也没付款、或者卖家关闭了订单。
     */
    enum STATUS {

    }

}
