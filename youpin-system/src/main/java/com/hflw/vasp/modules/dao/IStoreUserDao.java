package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.StoreUser;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;

public interface IStoreUserDao extends BaseRepository<StoreUser, Long>, Serializable {

    StoreUser findByPhone(String phone);

    StoreUser findByMiniOpenId(String miniOpenId);

}
