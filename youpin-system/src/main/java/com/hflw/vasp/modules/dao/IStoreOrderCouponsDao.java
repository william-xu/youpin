package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.StoreOrderCoupons;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IStoreOrderCouponsDao extends JpaRepository<StoreOrderCoupons, Long>, Serializable {
}
