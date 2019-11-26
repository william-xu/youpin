package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.StoreOrderGoods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IStoreOrderGoodsDao extends JpaRepository<StoreOrderGoods, Long>, Serializable {
}
