package com.hflw.vasp.eshop.modules.order.controller;

import com.hflw.vasp.annotation.SysLog;
import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.goods.service.GoodsService;
import com.hflw.vasp.eshop.modules.order.model.OrderDetails;
import com.hflw.vasp.eshop.modules.order.model.OrderModel;
import com.hflw.vasp.eshop.modules.order.model.OrderYoupinCardModel;
import com.hflw.vasp.eshop.modules.order.service.OrderService;
import com.hflw.vasp.eshop.modules.youpincard.service.YoupinCardService;
import com.hflw.vasp.modules.entity.Goods;
import com.hflw.vasp.modules.entity.Order;
import com.hflw.vasp.modules.entity.OrderGoods;
import com.hflw.vasp.modules.entity.YoupinCard;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController extends AbstractController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private YoupinCardService youpinCardService;

    /**
     * 订单列表
     */
    @GetMapping("/list")
    public R list() {
        List<OrderDetails> list = orderService.list(getUserId());
        return R.ok().data(list);
    }

    /**
     * 订单预览详情信息
     */
    @RequestMapping("/preview")
    public R preview(Long[] goodsIds, Integer[] goodsNums) {
        OrderDetails od = new OrderDetails();

        Order order = new Order();
        List<OrderGoods> ogList = new ArrayList<>();

        YoupinCard card = youpinCardService.findByUserId(getUserId());
        boolean flag = youpinCardService.verifyValid(card);

        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal payAmount = BigDecimal.ZERO;
        for (int i = 0; i < goodsIds.length; i++) {
            Goods g = goodsService.findById(goodsIds[i]);
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
        return R.ok().data(od);
    }

    /**
     * 提交优品卡订单，未支付
     */
    @SysLog
    @PostMapping("/ypcSubmit")
    public R youpinCardSubmit(OrderYoupinCardModel model) {
        Long orderId = orderService.youpinCardSubmit(getUserId(), model);
        return R.ok().data(orderId);
    }

    /**
     * 提交订单，未支付
     */
    @SysLog
    @PostMapping("/submit")
    public R submit(OrderModel model) {
        Long orderId = orderService.submit(getUserId(), model);
        return R.ok().data(orderId);
    }

    /**
     * 订单详情信息
     */
    @RequestMapping("/info")
    public R info(Long id) {
        OrderDetails od = orderService.getOrderDetailsById(id);
        return R.ok().data(od);
    }

    /**
     * 删除订单
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public R delete(Long id) {
        orderService.deleteOrder(id);
        return R.ok();
    }

}
