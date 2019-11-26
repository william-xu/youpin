package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IStoreDao extends JpaRepository<Store, Long>, Serializable {
}