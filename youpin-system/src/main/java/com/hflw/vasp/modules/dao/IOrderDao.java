package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Order;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;
import java.util.List;

public interface IOrderDao extends BaseRepository<Order, Long>, Serializable {

    List<Order> findAllByUserId(Long userId);

}
