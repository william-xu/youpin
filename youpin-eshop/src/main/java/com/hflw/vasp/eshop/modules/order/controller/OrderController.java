package com.hflw.vasp.eshop.modules.order.controller;

import com.hflw.vasp.annotation.SysLog;
import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.order.model.OrderDetails;
import com.hflw.vasp.eshop.modules.order.model.OrderModel;
import com.hflw.vasp.eshop.modules.order.model.OrderYoupinCardModel;
import com.hflw.vasp.eshop.modules.order.service.OrderService;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.modules.entity.Order;
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
     * 订单预览详情信息
     */
    @RequestMapping("/preview")
    public R preview(Long[] goodsIds, Integer[] goodsNums) {

        OrderDetails od = orderService.preview(getUserId(), goodsIds, goodsNums);

        return R.ok().data(od);
    }

    /**
     * 提交优品卡订单，未支付
     */
    @SysLog
    @PostMapping("/ypcSubmit")
    public R youpinCardSubmit(OrderYoupinCardModel model) {
        //校验是否存在未支付的优品卡订单
        Order ypOrder = orderService.findUnpayYoupinOrder(getUserId());
        if (ypOrder != null)
            throw BusinessException.create("存在未支付的优品卡订单");
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
        OrderDetails od = orderService.getDetailsById(id);
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
        orderService.logicDeleteById(id);
        return R.ok();
    }

}
