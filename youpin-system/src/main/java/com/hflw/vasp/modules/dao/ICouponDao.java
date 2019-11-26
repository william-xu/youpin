package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface ICouponDao extends JpaRepository<Coupon, Long>, Serializable {
}
