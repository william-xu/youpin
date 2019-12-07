package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Order;
import com.hflw.vasp.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

public interface IOrderDao extends BaseRepository<Order, Long>, Serializable {

    @Query("select o from Order o where o.userId=?1 order by o.id desc")
    List<Order> findAllByUserId(Long userId);

}
