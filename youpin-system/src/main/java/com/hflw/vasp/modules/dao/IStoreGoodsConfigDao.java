package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.StoreGoodsConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IStoreGoodsConfigDao extends JpaRepository<StoreGoodsConfig, Long>, Serializable {
}
