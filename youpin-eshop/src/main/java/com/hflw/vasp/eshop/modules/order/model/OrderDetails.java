package com.hflw.vasp.eshop.modules.order.model;

import com.hflw.vasp.modules.entity.Order;
import com.hflw.vasp.modules.entity.OrderAddress;
import com.hflw.vasp.modules.entity.OrderGoods;
import com.hflw.vasp.modules.entity.OrderLogistics;
import lombok.Data;

import java.util.List;

/**
 * 订单商品关系表
 *
 * @author liuyf
 * @date 2019-04-09 15:03:07
 */
@Data
public class OrderDetails {

    private Order order;

    private List<OrderGoods> goodsList;

    private OrderAddress address;

    private OrderLogistics logistics;

}
