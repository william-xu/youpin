package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.StoreOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IStoreOrderDao extends JpaRepository<StoreOrder, Long>, Serializable {
}
