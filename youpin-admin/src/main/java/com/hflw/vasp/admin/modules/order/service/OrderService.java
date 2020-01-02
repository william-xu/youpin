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
import java.util.*;

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
    private IYoupinCardDao youpinCardDao;

    @Autowired
    private ICustomerDao customerDao;

    @Autowired
    private EntityManager entityManager;

    public Pagination<OrderListModel> search(final OrderSearch search) throws Exception {
        StringBuffer sql = new StringBuffer();

        sql.append(" select o.id, ");
        sql.append("         o.user_id userId, ");
        sql.append("         o.order_no orderNo, ");
        sql.append("         o.parent_order_no parentOrderNo, ");
        sql.append("         o.status, ");
        sql.append("         o.pay_amount payAmount, ");
        sql.append("         o.promo_code promoCode, ");
//        sql.append("         DATE_FORMAT(o.create_time,'%Y-%m-%d %H:%i:%S') createTime, ");
        sql.append("         o.create_time createTime, ");
        sql.append("         case when o.type = 0 then oa.name else c.realname end name, ");
        sql.append("         case when o.type = 0 then oa.tel else c.phone end tel, ");
        sql.append("         ol.number, ");
        sql.append("         ypc.status ypcStatus ");
        sql.append(" FROM d_order o ");
        sql.append(" left join d_order_address oa on oa.order_id = o.id ");
        sql.append(" left join d_order_logistics ol on ol.order_id = o.id ");
        sql.append(" left join d_customer c on c.id = o.user_id ");
        sql.append(" left join d_youpin_card ypc on ypc.user_id = o.user_id ");
        sql.append(" where o.del_flag = 0 ");

        if (StringUtils.isNotBlank(search.getMerchantId())) {
            sql.append(" and o.promo_code = '").append(search.getMerchantId()).append("' ");
        }
        if (null != search.getOrderType()) {
            sql.append(" and o.type=").append(search.getOrderType());
        }
        if (null != search.getOrderStatus()) {
            sql.append(" and o.status=").append(search.getOrderStatus());
        }
        if (StringUtils.isNotBlank(search.getOrderNo())) {
            sql.append(" and o.order_no like '%").append(search.getOrderNo()).append("%' ");
        }
        if (StringUtils.isNotBlank(search.getParentOrderNo())) {
            sql.append(" and o.parent_order_no like '%").append(search.getParentOrderNo()).append("%' ");
        }
        if (StringUtils.isNotBlank(search.getSDate()) && StringUtils.isNotBlank(search.getEDate())) {
            sql.append(" and DATE(o.create_time) between '").append(search.getSDate()).append("' and '").append(search.getEDate()).append("' ");
        }
        if (null != search.getYpcStatus()) {
            if (1 == search.getYpcStatus()) {
                sql.append(" and ypc.status=").append(search.getYpcStatus());
                sql.append(" and now() between ypc.effective_date and ypc.expiration_date ");
            } else {
                sql.append(" and (ypc.status is null or ypc.status <> 1) ");
            }
        }
        sql.append(" order by o.id desc");

        String sqlStr = sql.toString();

        String count = "SELECT count(1) ";
        String substring = sqlStr.substring(0, sql.indexOf("FROM"));

        String countSql = sqlStr.replace(substring, count);

        Query query = entityManager.createNativeQuery(sqlStr);
        Query countQuery = entityManager.createNativeQuery(countSql);

        query.setFirstResult((search.getPageNumber() - 1) * 10);
        query.setMaxResults(search.getPageSize());

        long total = ((BigInteger) countQuery.getSingleResult()).longValue();

        List list = query.getResultList();

        List<OrderListModel> modelList = ReflectUtils.castEntity(list, OrderListModel.class);
        for (OrderListModel model : modelList) {
            YoupinCard card = youpinCardDao.findByUserId(model.getUserId().longValue());
            model.setYpcStatus(verifyValid(card) ? (byte) 1 : (byte) 2);
        }
        return new Pagination<>(total, modelList);
    }

    /**
     * 验证优品卡是否有效
     *
     * @param card
     * @return
     */
    public boolean verifyValid(YoupinCard card) {
        if (card == null) return false;
        if (card.getStatus() == null || card.getStatus() != 1) return false;

        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(card.getEffectiveDate());
        c2.setTime(card.getExpirationDate());
        return (c.after(c1) && c.before(c2)); //在权益有效期内
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
    public Page<Order> findOrderCriteria(final OrderSearch search) {
        Pageable pageable = PageRequest.of(search.getPageNumber() - 1, search.getPageSize(), Sort.Direction.DESC, "id");
        Specification<Order> specification = (Specification<Order>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Join<Order, OrderAddress> addressJoin = root.join("address", JoinType.LEFT);
            Join<Order, OrderLogistics> logisticsJoin = root.join("logistics", JoinType.LEFT);
            if (null != search.getOrderType()) {
                list.add(criteriaBuilder.equal(root.get("orderType").as(Integer.class), search.getOrderType()));
            }
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
        OrderLogistics existLogistics = orderLogisticsDao.findByOrderId(logistics.getOrderId());
        if (existLogistics != null)
            throw BusinessException.create("该订单已经绑定物流单号，请勿重复绑定！");

        logistics.setCreateTime(new Date());
        orderLogisticsDao.save(logistics);

        Order order = orderDao.getOne(logistics.getOrderId());
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
