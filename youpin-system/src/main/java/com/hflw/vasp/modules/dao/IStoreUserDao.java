package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.StoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IStoreUserDao extends JpaRepository<StoreUser, Long>, Serializable {

    StoreUser findByPhone(String phone);

    StoreUser findByMiniOpenId(String miniOpenId);

}
