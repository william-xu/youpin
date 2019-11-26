package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.StoreCouponConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IStoreCouponConfigDao extends JpaRepository<StoreCouponConfig, Long>, Serializable {
}
