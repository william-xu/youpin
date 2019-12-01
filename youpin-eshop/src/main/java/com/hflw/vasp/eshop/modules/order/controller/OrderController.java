package com.hflw.vasp.eshop.modules.order.controller;

import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.order.model.OrderDetails;
import com.hflw.vasp.eshop.modules.order.service.OrderService;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/submit")
    public R submit() {

        return R.ok();
    }

}
