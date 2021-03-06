package com.hflw.vasp.eshop.modules.order.service;

import com.google.common.base.Preconditions;
import com.hflw.vasp.enums.SysConstants;
import com.hflw.vasp.eshop.modules.order.model.OrderDetails;
import com.hflw.vasp.eshop.modules.order.model.OrderModel;
import com.hflw.vasp.eshop.modules.order.model.OrderYoupinCardModel;
import com.hflw.vasp.eshop.modules.youpincard.service.YoupinCardService;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.framework.components.PropertiesUtils;
import com.hflw.vasp.framework.service.RedisService;
import com.hflw.vasp.modules.dao.*;
import com.hflw.vasp.modules.entity.*;
import com.hflw.vasp.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {

    @Value("${youpincard.discount.ratio}")
    private Float discountRatio;

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
    private IOrderLogisticsDao orderLogisticsDao;

    @Autowired
    private IShopcarDao shopcarDao;

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
            //商品
            List<OrderGoods> ogList = orderGoodsDao.findAllByOrderId(o.getId());
            if (CollectionUtils.isNotEmpty(ogList)) {
                for (OrderGoods og : ogList) {
                    GoodsPicture gp = goodsPictureDao.findMainByGoodsId(og.getGoodsId());
                    if (gp != null) og.setPicUrl(getGoodsPicUrl(gp));
                }
            }
            //地址
            OrderAddress address = orderAddressDao.findByOrderId(o.getId());
            //物流
            OrderLogistics logistics = orderLogisticsDao.findByOrderId(o.getId());

            OrderDetails od = new OrderDetails();
            od.setOrder(o);
            od.setGoodsList(ogList);
            od.setAddress(address);
            od.setLogistics(logistics);
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
        //订单
        Order order = optionalOrder.get();
        //商品
        List<OrderGoods> orderGoods = orderGoodsDao.findAllByOrderId(order.getId());
        //地址
        OrderAddress address = orderAddressDao.findByOrderId(order.getId());
        //物流
        OrderLogistics logistics = orderLogisticsDao.findByOrderId(order.getId());

        OrderDetails od = new OrderDetails();
        od.setOrder(order);
        od.setGoodsList(orderGoods);
        od.setAddress(address);
        od.setLogistics(logistics);
        return od;
    }

    public Long youpinCardSubmit(Long userId, OrderYoupinCardModel model) {
        Date now = new Date();
        //订单主体
        Order order = new Order();
        order.setUserId(userId);
        order.setType(1);
        order.setOrderNo("YP" + redisService.getGlobalUniqueId());
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPayAmount(model.getPayAmount());
        order.setDiscountAmount(model.getOriginalCost().subtract(model.getPayAmount()));
        order.setPromoCode(model.getPromoCode());
        order.setStatus(0);
        order.setCreateTime(now);
        orderDao.save(order);

        // 子订单（送扫地机器人）
        subOrder(order, model.getAddressId());

        return order.getId();
    }

    public Long subOrder(Order parentOrder, Long addressId) {
        Date now = new Date();

        Goods g = goodsDao.getOne(4L);

        //订单主体
        Order order = new Order();
        order.setUserId(parentOrder.getUserId());
        order.setType(0);
        order.setOrderNo("B" + redisService.getGlobalUniqueId());
        order.setParentOrderNo(parentOrder.getOrderNo());
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setPayAmount(BigDecimal.ZERO);
        order.setDiscountAmount(g.getRetailPrice());
        order.setStatus(0);
        order.setCreateTime(now);
        orderDao.save(order);

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
            BigDecimal payPrice = flag ? g.getRetailPrice().multiply(new BigDecimal(discountRatio)).setScale(2, BigDecimal.ROUND_HALF_UP) : g.getRetailPrice();
            og.setPayPrice(payPrice);
            og.setGoodsNum(goodsNums[i]);
            GoodsPicture gp = goodsPictureDao.findMainByGoodsId(g.getId());
            og.setPicUrl(getGoodsPicUrl(gp));
            ogList.add(og);

            totalPrice = totalPrice.add(g.getRetailPrice().multiply(new BigDecimal(goodsNums[i])));
        }
        if (flag) {
            payAmount = totalPrice.multiply(new BigDecimal(discountRatio)).setScale(2, BigDecimal.ROUND_HALF_UP);
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

        //============ 以下为约束条件   ==============
        //1.用户id不能为空，且此用户确实是存在的
        Preconditions.checkNotNull(userId);

        List<OrderGoods> ogList = new ArrayList<>();

        //2.优品卡权益校验
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
            BigDecimal payPrice = flag ? g.getRetailPrice().multiply(new BigDecimal(discountRatio)).setScale(2, BigDecimal.ROUND_HALF_UP) : g.getRetailPrice();
            og.setPayPrice(payPrice);
            ogList.add(og);
        }
        if (flag) {
            payAmount = totalPrice.multiply(new BigDecimal(discountRatio)).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            payAmount = totalPrice;
        }
        log.info("提交总额：" + model.getPayAmount().toPlainString());
        log.info("商品总额：" + totalPrice.toPlainString());
        log.info("支付金额：" + payAmount.toPlainString());
        if (payAmount.compareTo(model.getPayAmount()) != 0)
            throw BusinessException.create("金额有误，请刷新购物车后重试");

        Date now = new Date();
        //订单主体
        Order order = new Order();
        order.setUserId(userId);
        order.setType(0);
        order.setOrderNo("B" + redisService.getGlobalUniqueId());
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

            //删除购物车商品
            shopcarDao.deleteByUserIdAndAndGoodsId(userId, og.getGoodsId());
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
        Order order = orderDao.getOne(id);

        if (!SysConstants.DeleteStatus.normal.getCode().equals(order.getDelFlag()))
            throw BusinessException.create("订单已删除，请刷新重试");

        if (order.getType() == 1) {//优品卡订单
            //同步删除附属订单
            Order subOrder = orderDao.findByParentOrderNo(order.getOrderNo());
            if (subOrder != null) orderDao.logicDeleteById(subOrder.getId());
            return;
        } else if (order.getType() == 0) {//商品订单
            if (StringUtils.isNotEmpty(order.getParentOrderNo())) {//优品卡附属订单
                Order parentOrder = orderDao.findByParentOrderNo(order.getParentOrderNo());

                //父订单存在且没有被删除，子订单不可被删除
                if (parentOrder != null && SysConstants.DeleteStatus.normal.getCode().equals(parentOrder.getDelFlag())) {
                    throw BusinessException.create("赠送商品订单不可被单独删除");
                }
            }
        }
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

    public Order findValidYoupinOrder(Long userId) {
        return orderDao.findValidYoupinOrder(userId);
    }

}
