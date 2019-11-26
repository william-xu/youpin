package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IGoodsDao extends JpaRepository<Goods, Long>, Serializable {
}
