package com.hflw.vasp.eshop.modules.order.service;

import com.hflw.vasp.eshop.modules.order.model.OrderDetails;
import com.hflw.vasp.eshop.modules.order.model.OrderModel;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.modules.dao.*;
import com.hflw.vasp.modules.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IGoodsDao goodsDao;

    @Autowired
    private IOrderGoodsDao orderGoodsDao;

    @Autowired
    private IOrderAddress orderAddressDao;

    @Autowired
    private ICustomerAddressDao customerAddressDao;

    public List<OrderDetails> list(Long userId) {
        List<Order> oList = orderDao.findAllByUserId(userId);

        List<OrderDetails> list = new ArrayList<>();
        for (Order o : oList) {
            List<OrderGoods> orderGoods = orderGoodsDao.findAllByOrderId(o.getId());

            OrderDetails od = new OrderDetails();
            od.setOrder(o);
            od.setGoodsList(orderGoods);
            list.add(od);
        }
        return list;
    }

    public OrderDetails getOrderDetailsById(Long id) {
        Optional<Order> optionalOrder = orderDao.findById(id);
        Order order = optionalOrder.get();
        List<OrderGoods> orderGoods = orderGoodsDao.findAllByOrderId(order.getId());

        Optional<OrderAddress> optionalAddress = orderAddressDao.findById(order.getId());
        OrderAddress address = optionalAddress.get();

        OrderDetails od = new OrderDetails();
        od.setOrder(order);
        od.setGoodsList(orderGoods);
        od.setAddress(address);
        return od;
    }

    public void submit(Long userId, OrderModel model) {

        List<OrderGoods> ogList = new ArrayList<>();

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (int i = 0; i < model.getGoodsIds().length; i++) {
            Long goodsId = model.getGoodsIds()[i];
            Integer goodsNum = model.getGoodsNums()[i];
            Goods goods = goodsDao.getOne(goodsId);
            totalPrice = totalPrice.add(goods.getRetailPrice().multiply(new BigDecimal(goodsNum)));

            OrderGoods og = new OrderGoods();
            og.setGoodsId(goodsId);
            og.setGoodsName(goods.getName());
            og.setGoodsNum(goodsNum);
            og.setGoodsPrice(goods.getRetailPrice());
            ogList.add(og);
        }

        System.out.println(totalPrice.toPlainString());
        if (!totalPrice.equals(model.getPayAmount()))
            throw BusinessException.create(6546, "金额有误，请刷新购物车后重试");

        Date now = new Date();

        //订单主体
        Order order = new Order();
        order.setUserId(userId);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPayAmount(totalPrice);
        order.setStatus(0);
        order.setCreateTime(now);
        orderDao.save(order);

        //订单商品
        for (OrderGoods og : ogList) {
            og.setOrderId(order.getId());
            orderGoodsDao.save(og);
        }

        //订单地址
        Optional<CustomerAddress> optionalAddress = customerAddressDao.findById(model.getAddressId());
        CustomerAddress selectedAddress = optionalAddress.get();
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setOrderId(order.getId());
        orderAddress.setName(selectedAddress.getName());
        orderAddress.setTel(selectedAddress.getTel());
        String fullAddress = selectedAddress.getProvince() + selectedAddress.getCity() + selectedAddress.getArea() + selectedAddress.getAddress();
        orderAddress.setAddress(fullAddress);
        orderAddressDao.save(orderAddress);
    }

}
