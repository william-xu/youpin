package com.hflw.vasp.admin.modules.order.service;

import com.hflw.vasp.admin.common.enums.OrderStatus;
import com.hflw.vasp.admin.modules.order.dto.OrderSearch;
import com.hflw.vasp.admin.modules.order.model.OrderDetails;
import com.hflw.vasp.admin.modules.order.model.OrderListModel;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.modules.dao.*;
import com.hflw.vasp.modules.entity.*;
import com.hflw.vasp.utils.ReflectUtils;
import com.hflw.vasp.utils.StringUtils;
import com.hflw.vasp.web.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IOrderGoodsDao orderGoodsDao;

    @Autowired
    private IOrderAddress orderAddressDao;

    @Autowired
    private IOrderLogisticsDao orderLogisticsDao;

    @Autowired
    private ICustomerDao customerDao;

    @Autowired
    private IYoupinCardDao youpinCardDao;

    @Autowired
    private EntityManager entityManager;

    public Pagination<OrderListModel> search(final OrderSearch search, cn.hutool.db.Page page) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" select o.id, ");
        sql.append("         o.user_id userId, ");
        sql.append("         o.order_no orderNo, ");
        sql.append("         o.parent_order_no parentOrderNo, ");
        sql.append("         o.status, ");
        sql.append("         o.pay_amount payAmount, ");
        sql.append("         oa.name name, ");
        sql.append("         oa.tel tel, ");
        sql.append("         ol.number logisticsNo, ");
        sql.append("         o.create_time createTime ");
        sql.append(" FROM d_order o ");
        sql.append(" left join d_order_address oa on oa.order_id = o.id ");
        sql.append(" left join d_order_logistics ol on ol.order_id = o.id and ol.del_flag = 0 ");
        sql.append(" left join d_customer c on c.id = o.user_id ");
        sql.append(" where o.del_flag = 0 and o.type= 0 ");

        if (null != search.getOrderStatus()) {
            sql.append(" and o.status=").append(search.getOrderStatus());
        }
        if (StringUtils.isNotBlank(search.getOrderNo())) {
            sql.append(" and o.order_no like '%").append(search.getOrderNo()).append("%' ");
        }
        if (StringUtils.isNotBlank(search.getParentOrderNo())) {
            sql.append(" and o.parent_order_no like '%").append(search.getParentOrderNo()).append("%' ");
        }
        if (StringUtils.isNotBlank(search.getPhone())) {
            sql.append(" and oa.tel like '%").append(search.getPhone()).append("%' ");
        }
        if (StringUtils.isNotBlank(search.getSDate()) && StringUtils.isNotBlank(search.getEDate())) {
            sql.append(" and DATE(o.create_time) between '").append(search.getSDate()).append("' and '").append(search.getEDate()).append("' ");
        }
        sql.append(" order by o.id desc");

        String sqlStr = sql.toString();

        String count = "SELECT count(1) ";
        String substring = sqlStr.substring(0, sql.indexOf("FROM"));

        String countSql = sqlStr.replace(substring, count);

        Query query = entityManager.createNativeQuery(sqlStr);
        Query countQuery = entityManager.createNativeQuery(countSql);

        //设置分页
        if (page != null) {
            query.setFirstResult((page.getPageNumber() - 1) * 10);
            query.setMaxResults(page.getPageSize());
        }

        long total = ((BigInteger) countQuery.getSingleResult()).longValue();

        List list = query.getResultList();

        List<OrderListModel> modelList = ReflectUtils.castEntity(list, OrderListModel.class);
        return new Pagination<>(total, modelList);
    }


    public OrderDetails getDetailsById(Long id) {
        Optional<Order> optionalOrder = orderDao.findById(id);
        Order order = optionalOrder.get();
        //商品
        List<OrderGoods> orderGoods = orderGoodsDao.findAllByOrderId(order.getId());
        //地址
        OrderAddress address = orderAddressDao.findByOrderId(order.getId());
        //物流
        OrderLogistics logistics = orderLogisticsDao.findByOrderId(order.getId());
        //购买人
        Customer customer = customerDao.getOne(order.getUserId());

        OrderDetails od = new OrderDetails();
        od.setOrder(order);
        od.setGoodsList(orderGoods);
        od.setAddress(address);
        od.setLogistics(logistics);
        od.setCustomer(customer);
        return od;
    }

    /**
     * 分页查询，多表多条件查询 限制太多  需要在实体中添加 外键约束
     * 这个需要 order 有addressId和logisticsId外键
     *
     * @param search
     * @return
     */
    public Page<Order> findOrderCriteria(final OrderSearch search, cn.hutool.db.Page page) {
        Pageable pageable = PageRequest.of(page.getPageNumber() - 1, page.getPageSize(), Sort.Direction.DESC, "id");
        Specification<Order> specification = (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Join<Order, OrderAddress> addressJoin = root.join("address", JoinType.LEFT);
            Join<Order, OrderLogistics> logisticsJoin = root.join("logistics", JoinType.LEFT);
            list.add(criteriaBuilder.equal(root.get("orderType").as(Integer.class), 0));
            if (StringUtils.isNotEmpty(search.getOrderNo())) {
                list.add(criteriaBuilder.like(root.get("orderNo").as(String.class), "%" + search.getOrderNo() + "%"));
            }
            if (StringUtils.isNotEmpty(search.getName())) {
                list.add(criteriaBuilder.like(addressJoin.get("name").as(String.class), "%" + search.getName() + "%"));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        };
        return orderDao.findAll(specification, pageable);
    }

    /**
     * 绑定物流信息
     *
     * @param logistics
     */
    public void bindLogistics(OrderLogistics logistics) {
        Order order = orderDao.getOne(logistics.getOrderId());
        if (order.getStatus() == OrderStatus.WP.getValue())
            throw BusinessException.create("未付款订单无法绑定物流信息！");

        OrderLogistics existLogistics = orderLogisticsDao.findByOrderId(logistics.getOrderId());

        //删除旧物流
        if (existLogistics != null) orderLogisticsDao.logicDeleteById(existLogistics.getId());

        //绑定新物流
        logistics.setCreateTime(new Date());
        orderLogisticsDao.save(logistics);

        //更新订单状态
        order.setStatus(OrderStatus.AD.getValue());
        order.setUpdateTime(new Date());
        orderDao.save(order);
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
        if (order.getType() == 1) {
            //2019/12/20 修改优品卡激活状态
            YoupinCard card = youpinCardDao.findByUserId(order.getUserId());
            invalidYoupinCard(card);
        }
        order.setStatus(status);
        order.setUpdateTime(new Date());
        orderDao.save(order);
    }

    public void invalidYoupinCard(YoupinCard card) {
        if (card == null) return;

        card.setStatus((byte) 2);
        card.setUpdateTime(new Date());
        youpinCardDao.save(card);
    }

}
