package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Coupon;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;

public interface ICouponDao extends BaseRepository<Coupon, Long>, Serializable {
}
