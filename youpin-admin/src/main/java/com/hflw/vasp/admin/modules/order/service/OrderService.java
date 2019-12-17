package com.hflw.vasp.admin.modules.order.service;

import com.hflw.vasp.admin.modules.order.dto.OrderQuery;
import com.hflw.vasp.modules.dao.IOrderDao;
import com.hflw.vasp.modules.dao.IOrderLogisticsDao;
import com.hflw.vasp.modules.entity.Order;
import com.hflw.vasp.modules.entity.OrderAddress;
import com.hflw.vasp.modules.entity.OrderLogistics;
import com.hflw.vasp.modules.model.OrderListModel;
import com.hflw.vasp.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IOrderLogisticsDao orderLogisticsDao;

    /**
     * 分页查询
     *
     * @param orderQuery
     * @return
     */
    public Page<Order> findOrderCriteria(final OrderQuery orderQuery) {
        Pageable pageable = PageRequest.of(orderQuery.getPageNumber() - 1, orderQuery.getPageSize(), Sort.Direction.DESC, "id");
        Page<Order> orderPage = orderDao.findAll((Specification<Order>) (root, query, cb) -> {

            List<Predicate> list = new ArrayList<>();

//            Join<Order, OrderAddress> addressJoin = root.join("orderId", JoinType.LEFT);
//            Predicate p1 = cb.equal(addressJoin.get("orderId").as(Long.class), root.get("id").as(Long.class));
//            list.add(p1);

            if (StringUtils.isNotEmpty(orderQuery.getOrderNo())) {
                list.add(cb.equal(root.get("orderNo").as(String.class), orderQuery.getOrderNo()));
            }
            if (null != orderQuery.getOrderType()) {
                list.add(cb.equal(root.get("type").as(Integer.class), orderQuery.getOrderType()));
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        }, pageable);
        return orderPage;
    }

    public void findOrderCriteria2(final OrderQuery orderQuery){
        Pageable pageable = PageRequest.of(orderQuery.getPageNumber() - 1, orderQuery.getPageSize(), Sort.Direction.DESC, "id");
//        Page<OrderListModel> page = orderDao.findOrderCriteria(pageable);
//        System.out.println(page);
    }

    /**
     * 绑定物流信息
     *
     * @param logistics
     */
    public void bindLogistics(OrderLogistics logistics) {
        logistics.setCreateTime(new Date());
        orderLogisticsDao.save(logistics);
    }

    /**
     * 修改订单状态
     *
     * @param orderId
     * @param status
     */
    public void change(Long orderId, Integer status) {
        Optional<Order> optional = orderDao.findById(orderId);
        Order order = optional.get();

        order.setStatus(status);
        order.setUpdateTime(new Date());
        orderDao.save(order);
    }

}
