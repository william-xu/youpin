package com.hflw.vasp.admin.modules.order.model;

import com.hflw.vasp.modules.entity.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 订单商品关系表
 *
 * @author liuyf
 * @date 2019-04-09 15:03:07
 */
@Data
public class OrderDetails implements Serializable {

    private Order order;

    private List<OrderGoods> goodsList;

    private OrderAddress address;

    private OrderLogistics logistics;

    private Customer customer;

}
