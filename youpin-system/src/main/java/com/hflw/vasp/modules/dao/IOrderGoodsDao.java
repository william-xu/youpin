package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.OrderGoods;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;
import java.util.List;

public interface IOrderGoodsDao extends BaseRepository<OrderGoods, Long>, Serializable {

    List<OrderGoods> findAllByGoodsId(Long userId);

}
