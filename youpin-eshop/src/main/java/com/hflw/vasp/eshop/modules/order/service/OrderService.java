package com.hflw.vasp.eshop.modules.order.service;

import com.hflw.vasp.eshop.modules.order.model.OrderDetails;
import com.hflw.vasp.eshop.modules.order.model.OrderModel;
import com.hflw.vasp.eshop.modules.youpincard.service.YoupinCardService;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.modules.dao.*;
import com.hflw.vasp.modules.entity.*;
import com.hflw.vasp.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IGoodsDao goodsDao;

    @Autowired
    private IGoodsPictureDao goodsPictureDao;

    @Autowired
    private IOrderGoodsDao orderGoodsDao;

    @Autowired
    private IOrderAddress orderAddressDao;

    @Autowired
    private ICustomerAddressDao customerAddressDao;

    @Autowired
    private YoupinCardService youpinCardService;

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

        //优品卡权益校验
        YoupinCard card = youpinCardService.findByUserId(userId);
        boolean flag = youpinCardService.verifyValid(card);

        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal payAmount = BigDecimal.ZERO;
        for (int i = 0; i < model.getGoodsIds().length; i++) {
            Long goodsId = model.getGoodsIds()[i];
            Integer goodsNum = model.getGoodsNums()[i];
            Goods g = goodsDao.getOne(goodsId);
            totalPrice = totalPrice.add(g.getRetailPrice().multiply(new BigDecimal(goodsNum)));

            OrderGoods og = new OrderGoods();
            og.setGoodsId(goodsId);
            og.setGoodsName(g.getName());
            og.setGoodsNum(goodsNum);
            og.setGoodsPrice(g.getRetailPrice());
            BigDecimal payPrice = flag ? g.getRetailPrice().multiply(new BigDecimal("0.85")).setScale(2, BigDecimal.ROUND_HALF_UP) : g.getRetailPrice();
            og.setPayPrice(payPrice);
            ogList.add(og);
        }
        if (flag) {
            payAmount = totalPrice.multiply(new BigDecimal("0.85"));
        } else {
            payAmount = totalPrice;
        }
        logger.info("提交总额：" + model.getPayAmount().toPlainString());
        logger.info("商品总额：" + totalPrice.toPlainString());
        logger.info("支付金额：" + payAmount.toPlainString());
        if (payAmount.compareTo(model.getPayAmount()) != 0)
            throw BusinessException.create(6546, "金额有误，请刷新购物车后重试");

        Date now = new Date();
        //订单主体
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNo(SnowFlake.nextSerialNumber());
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPayAmount(payAmount);
        order.setDiscountAmount(totalPrice.subtract(payAmount));
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
