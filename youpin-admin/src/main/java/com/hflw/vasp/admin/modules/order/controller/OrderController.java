package com.hflw.vasp.admin.modules.order.controller;


import com.hflw.vasp.admin.modules.AbstractController;
import com.hflw.vasp.admin.modules.order.dto.OrderLogisticsDTO;
import com.hflw.vasp.admin.modules.order.dto.OrderSearch;
import com.hflw.vasp.admin.modules.order.model.OrderListModel;
import com.hflw.vasp.admin.modules.order.service.OrderService;
import com.hflw.vasp.modules.entity.OrderLogistics;
import com.hflw.vasp.web.Pagination;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author liuyf
 * @Title UserController.java
 * @Package com.hflw.vasp.controller
 * @Description 订单
 * @date 2019年10月24日 下午2:02:54
 */
@RestController
@RequestMapping(value = "/order")
@Validated
public class OrderController extends AbstractController {

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/search")
    public R query(OrderSearch search) throws Exception {
        Pagination<OrderListModel> page = orderService.search(search);
        return R.ok().putPageData(page);
    }

    @GetMapping(value = "/info")
    public R query(Long id) {

        return R.ok();
    }

    /**
     * 绑定物流信息
     *
     * @param logisticsDTO
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/bindlogistics")
    public R bindLogistics(@Valid OrderLogisticsDTO logisticsDTO, BindingResult bindingResult) {
        checkDTOParams(bindingResult);

        OrderLogistics logistics = logisticsDTO.convertTo();
        orderService.bindLogistics(logistics);

        return R.ok();
    }

    /**
     * 修改商品订单
     *
     * @param orderId
     * @param status
     * @return
     */
    @PostMapping(value = "/change")
    public R change(@NotNull Long orderId, @NotNull Integer status) {
        orderService.change(orderId, status);
        return R.ok();
    }

    /**
     * 修改优品订单
     *
     * @param orderId
     * @param status
     * @return
     */
    @PostMapping(value = "/changeYoupin")
    public R changeYoupin(@NotNull Long orderId, @NotNull Integer status) {
        orderService.change(orderId, status);
        return R.ok();
    }

}
