package com.hflw.vasp.system.dao;

import com.hflw.vasp.system.entity.SysEmpty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICommonDao extends JpaRepository<SysEmpty, Long> {
}
