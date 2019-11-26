package com.hflw.vasp.system.dao;

import com.hflw.vasp.system.entity.SysProvince;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface ISysProvinceDao extends JpaRepository<SysProvince, Long>, Serializable {
}
