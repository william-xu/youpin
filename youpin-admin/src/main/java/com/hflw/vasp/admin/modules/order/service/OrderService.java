package com.hflw.vasp.admin.modules.order.service;

import com.hflw.vasp.admin.modules.order.dto.OrderSearch;
import com.hflw.vasp.admin.modules.order.model.OrderListModel;
import com.hflw.vasp.modules.dao.IOrderDao;
import com.hflw.vasp.modules.dao.IOrderLogisticsDao;
import com.hflw.vasp.modules.entity.Order;
import com.hflw.vasp.modules.entity.OrderAddress;
import com.hflw.vasp.modules.entity.OrderLogistics;
import com.hflw.vasp.utils.ReflectUtils;
import com.hflw.vasp.utils.StringUtils;
import com.hflw.vasp.web.Pagination;
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

@Service
public class OrderService {

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IOrderLogisticsDao orderLogisticsDao;

    @Autowired
    private EntityManager entityManager;

    public Pagination<OrderListModel> search(final OrderSearch search) throws Exception {
        StringBuffer sql = new StringBuffer();

        sql.append(" select o.id, ");
        sql.append("         o.order_no orderNo, ");
        sql.append("         o.status, ");
        sql.append("         o.pay_amount payAmount, ");
        sql.append("         o.promo_code promoCode, ");
        sql.append("         DATE_FORMAT(o.create_time,'%Y-%m-%d %H:%i:%S') createTime, ");
        sql.append("         oa.name, ");
        sql.append("         oa.tel, ");
        sql.append("         ol.number ");
        sql.append(" FROM d_order o ");
        sql.append(" left join d_order_address oa on oa.order_id = o.id ");
        sql.append(" left join d_order_logistics ol on ol.order_id = o.id ");
        sql.append(" where 1 = 1 ");

        if (null != search.getOrderType()) {
            sql.append(" and o.type=").append(search.getOrderType());
        }
        if (null != search.getOrderStatus()) {
            sql.append(" and o.status=").append(search.getOrderStatus());
        }
        if (StringUtils.isNotBlank(search.getOrderNo())) {
            sql.append(" and o.order_no like '%").append(search.getOrderNo()).append("%' ");
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

        query.setFirstResult((search.getPageNumber() - 1) * 10);
        query.setMaxResults(search.getPageSize());

        long total = ((BigInteger) countQuery.getSingleResult()).longValue();

        List list = query.getResultList();

        List<OrderListModel> modelList = ReflectUtils.castEntity(list, OrderListModel.class);

        return new Pagination<>(total, modelList);
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
