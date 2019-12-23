package com.hflw.vasp.modules.service;

import com.hflw.vasp.modules.dao.IOrderDao;
import com.hflw.vasp.modules.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private IOrderDao orderDao;

    /**
     * 取消超时未支付订单
     *
     * @return
     */
    public List<Order> cancelTimeoutUnpayOrder() {
        List<Order> list = orderDao.findAllInvalidByCreateTime(60);
        return list;
    }

}
