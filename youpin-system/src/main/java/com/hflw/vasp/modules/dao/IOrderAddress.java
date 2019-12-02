package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.OrderAddress;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;

public interface IOrderAddress extends BaseRepository<OrderAddress, Long>, Serializable {
}
