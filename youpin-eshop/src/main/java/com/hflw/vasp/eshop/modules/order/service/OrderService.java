package com.hflw.vasp.eshop.modules.order.service;

import com.hflw.vasp.eshop.modules.order.model.OrderDetails;
import com.hflw.vasp.eshop.modules.order.model.OrderModel;
import com.hflw.vasp.eshop.modules.order.model.OrderYoupinCardModel;
import com.hflw.vasp.eshop.modules.youpincard.service.YoupinCardService;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.framework.components.PropertiesUtils;
import com.hflw.vasp.framework.service.RedisService;
import com.hflw.vasp.modules.dao.*;
import com.hflw.vasp.modules.entity.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
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

    @Autowired
    private RedisService redisService;

    private String getGoodsPicUrl(GoodsPicture gp) {
        String bossImgsUrl = PropertiesUtils.getProperty("boss.imgs.url");
        return bossImgsUrl + gp.getGoodsId() + File.separator + gp.getPicUrl();
    }

    public List<OrderDetails> list(Long userId) {
        List<Order> oList = orderDao.findAllByUserId(userId);

        List<OrderDetails> list = new ArrayList<>();
        for (Order o : oList) {
            List<OrderGoods> ogList = orderGoodsDao.findAllByOrderId(o.getId());
            if (CollectionUtils.isNotEmpty(ogList)) {
                for (OrderGoods og : ogList) {
                    GoodsPicture gp = goodsPictureDao.findMainByGoodsId(og.getGoodsId());
                    if (gp != null) og.setPicUrl(getGoodsPicUrl(gp));
                }
            }
            OrderDetails od = new OrderDetails();
            od.setOrder(o);
            od.setGoodsList(ogList);
            list.add(od);
        }
        return list;
    }

    public Order findById(Long id) {
        Optional<Order> optionalOrder = orderDao.findById(id);
        Order order = optionalOrder.get();
        return order;
    }

    public OrderDetails getDetailsById(Long id) {
        Optional<Order> optionalOrder = orderDao.findById(id);
        Order order = optionalOrder.get();

        List<OrderGoods> orderGoods = orderGoodsDao.findAllByOrderId(order.getId());

        Optional<OrderAddress> optionalAddress = orderAddressDao.findById(order.getId());
        OrderAddress address = optionalAddress.orElse(null);

        OrderDetails od = new OrderDetails();
        od.setOrder(order);
        od.setGoodsList(orderGoods);
        od.setAddress(address);
        return od;
    }

    public Long youpinCardSubmit(Long userId, OrderYoupinCardModel model) {
        Date now = new Date();
        //订单主体
        Order order = new Order();
        order.setUserId(userId);
        order.setType(1);
        order.setOrderNo(String.valueOf(redisService.getGlobalUniqueId()));
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPayAmount(model.getPayAmount());
        order.setDiscountAmount(model.getOriginalCost().subtract(model.getPayAmount()));
        order.setStatus(0);
        order.setCreateTime(now);
        orderDao.save(order);

        // 子订单（送扫地机器人）
        subOrder(order, model.getAddressId());

        return order.getId();
    }

    public Long subOrder(Order parentOrder, Long addressId) {
        Date now = new Date();
        //订单主体
        Order order = new Order();
        order.setUserId(parentOrder.getUserId());
        order.setType(0);
        order.setOrderNo(String.valueOf(redisService.getGlobalUniqueId()));
        order.setParentOrderNo(parentOrder.getParentOrderNo());
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPayAmount(BigDecimal.ZERO);
        order.setDiscountAmount(new BigDecimal("699.00"));
        order.setStatus(0);
        order.setCreateTime(now);
        orderDao.save(order);

        Goods g = goodsDao.getOne(4L);
        OrderGoods og = new OrderGoods();
        og.setOrderId(order.getId());
        og.setGoodsId(g.getId());
        og.setGoodsName(g.getName());
        og.setGoodsPrice(g.getRetailPrice());
        og.setPayPrice(BigDecimal.ZERO);
        og.setGoodsNum(1);
        og.setPicUrl(g.getPicUrl());
        orderGoodsDao.save(og);

        Optional<CustomerAddress> optionalAddress = customerAddressDao.findById(addressId);
        CustomerAddress selectedAddress = optionalAddress.get();
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setOrderId(order.getId());
        orderAddress.setName(selectedAddress.getName());
        orderAddress.setTel(selectedAddress.getTel());
        String fullAddress = selectedAddress.getProvince() + selectedAddress.getCity() + selectedAddress.getArea() + selectedAddress.getAddress();
        orderAddress.setAddress(fullAddress);
        orderAddressDao.save(orderAddress);
        return order.getId();
    }

    public OrderDetails preview(Long userId, Long[] goodsIds, Integer[] goodsNums) {
        OrderDetails od = new OrderDetails();

        Order order = new Order();
        List<OrderGoods> ogList = new ArrayList<>();

        YoupinCard card = youpinCardService.findByUserId(userId);
        boolean flag = youpinCardService.verifyValid(card);

        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal payAmount = BigDecimal.ZERO;
        for (int i = 0; i < goodsIds.length; i++) {
            Goods g = goodsDao.getOne(goodsIds[i]);
            OrderGoods og = new OrderGoods();
            og.setGoodsId(g.getId());
            og.setGoodsName(g.getName());
            og.setGoodsPrice(g.getRetailPrice());
            BigDecimal payPrice = flag ? g.getRetailPrice().multiply(new BigDecimal("0.85")).setScale(2, BigDecimal.ROUND_HALF_UP) : g.getRetailPrice();
            og.setPayPrice(payPrice);
            og.setGoodsNum(goodsNums[i]);
            og.setPicUrl(g.getPicUrl());
            ogList.add(og);

            totalPrice = totalPrice.add(g.getRetailPrice().multiply(new BigDecimal(goodsNums[i])));
        }
        if (flag) {
            payAmount = totalPrice.multiply(new BigDecimal("0.85")).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            payAmount = totalPrice;
        }
        order.setPayAmount(payAmount);
        order.setTotalAmount(totalPrice);
        BigDecimal discountAmount = order.getTotalAmount().subtract(order.getPayAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
        order.setDiscountAmount(discountAmount);
        order.setCreateTime(new Date());
        od.setOrder(order);
        od.setGoodsList(ogList);

        return od;
    }

    public Long submit(Long userId, OrderModel model) {

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
        order.setType(0);
        order.setOrderNo(String.valueOf(redisService.getGlobalUniqueId()));
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
        return order.getId();
    }

    public void logicDeleteById(Long id) {
        orderDao.logicDeleteById(id);
    }

    public void update(Order order) {
        orderDao.save(order);
    }

    public Order findByParentOrderNo(String orderNo) {
        return orderDao.findByParentOrderNo(orderNo);
    }

    public Order findUnpayYoupinOrder(Long userId) {
        return orderDao.findUnpayYoupinOrder(userId);
    }

}
