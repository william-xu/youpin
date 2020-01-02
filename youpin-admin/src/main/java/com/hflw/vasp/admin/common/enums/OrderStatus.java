package com.hflw.vasp.admin.common.enums;

public enum OrderStatus {

//    0.待付款：代表买家下单了但是还没有付款。
//    1.待发货（同待接单）：代表买家付款了卖家还没有发货。
//    2.已发货（同待收货）：代表卖家已经发货并寄出商品了。
//    3.已完成（同待评价）：代表买家已经确认收到货了。
//    4.已关闭（同已取消）：代表订单过期了买家也没付款、或者卖家关闭了订单。
//    5.退货退款等待完善，后面拆分？

    WP(0, "待付款"),//waiting for payment
    WD(1, "待发货"),//waiting for delivery
    AD(2, "已发货"),//already delivered
    AF(3, "已完成"),//already finished
    AC(4, "已关闭"),//already canceled
    RR(5, "退货退款"),//return refund
    WE(6, "已评价");//waiting for evaluation

    private OrderStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private int value;
    private String desc;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
