package com.hflw.vasp.eshop.modules.order.service;

import com.hflw.vasp.eshop.modules.order.model.OrderDetails;
import com.hflw.vasp.eshop.modules.shopcar.model.ShopcarDetail;
import com.hflw.vasp.modules.dao.IOrderDao;
import com.hflw.vasp.modules.dao.IOrderGoodsDao;
import com.hflw.vasp.modules.entity.Order;
import com.hflw.vasp.modules.entity.OrderGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IOrderGoodsDao orderGoodsDao;

    public List<OrderDetails> list(Long userId) {
        List<Order> oList = orderDao.findAllByUserId(userId);

        List<OrderDetails> list = new ArrayList<>();
        for (Order o : oList) {
            List<OrderGoods> orderGoods = orderGoodsDao.findAllByGoodsId(userId);

            OrderDetails od = new OrderDetails();
            od.setOrder(o);
            od.setGoodsList(orderGoods);
            list.add(od);
        }
        return list;
    }

}
