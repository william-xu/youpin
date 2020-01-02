package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Order;
import com.hflw.vasp.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

public interface IOrderDao extends BaseRepository<Order, Long>, JpaSpecificationExecutor<Order>, Serializable {

    @Query("select o from Order o where o.userId=?1 and o.delFlag = 0 order by o.id desc")
    List<Order> findAllByUserId(Long userId);

    @Query("select o from Order o where o.delFlag = 0 and o.status =?1 order by o.id desc")
    List<Order> findAllByStatus(Integer status);

    @Query(value = "select * from d_order o where o.del_flag = 0 and o.status =0 and timestampdiff(MINUTE, create_time, NOW()) > ?1", nativeQuery = true)
    List<Order> findAllInvalidByCreateTime(Integer minute);

    @Transactional
    @Modifying
    @Query("update Order o set o.delFlag = -1 where o.id=?1")
    void logicDeleteById(Long id);

    @Query("select o from Order o where o.userId=?1 and o.delFlag = 0 and o.type = 1 and o.status = 0 order by o.id desc")
    Order findUnpayYoupinOrder(Long userId);

    @Query("select o from Order o where o.userId=?1 and o.delFlag = 0 and o.type = 1 and o.status = 1 order by o.id desc")
    Order findValidYoupinOrder(Long userId);

    Order findByParentOrderNo(String orderNo);

}
