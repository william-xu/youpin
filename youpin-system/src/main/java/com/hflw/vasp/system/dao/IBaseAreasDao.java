package com.hflw.vasp.system.dao;

import com.hflw.vasp.system.entity.SysArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IBaseAreasDao extends JpaRepository<SysArea, Long>, Serializable {
}
