package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.OrderLogistics;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;

public interface IOrderLogisticsDao extends BaseRepository<OrderLogistics, Long>, Serializable {

    OrderLogistics findByOrderId(Long orderId);

}
