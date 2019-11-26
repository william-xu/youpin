package com.hflw.vasp.system.dao;

import com.hflw.vasp.system.entity.SysCity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IBaseCitiesDao extends JpaRepository<SysCity, Long>, Serializable {
}
