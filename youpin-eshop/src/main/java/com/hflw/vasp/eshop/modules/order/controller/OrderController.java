package com.hflw.vasp.eshop.modules.order.controller;

import com.hflw.vasp.annotation.SysLog;
import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.order.model.OrderDetails;
import com.hflw.vasp.eshop.modules.order.model.OrderModel;
import com.hflw.vasp.eshop.modules.order.service.OrderService;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController extends AbstractController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单列表
     */
    @GetMapping("/list")
    public R list() {
        List<OrderDetails> list = orderService.list(getUserId());
        return R.ok().data(list);
    }

    /**
     * 提交订单，未支付
     */
    @SysLog
    @PostMapping("/submit")
    public R submit(OrderModel model) {
        orderService.submit(getUserId(), model);
        return R.ok();
    }

    /**
     * 商品详情信息
     */
    @RequestMapping("/info")
    public R info(Long id) {
        OrderDetails od = orderService.getOrderDetailsById(id);
        return R.ok().data(od);
    }

}
