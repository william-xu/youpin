package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Shopcar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IShopcarDao extends JpaRepository<Shopcar, Long>, Serializable {
}
