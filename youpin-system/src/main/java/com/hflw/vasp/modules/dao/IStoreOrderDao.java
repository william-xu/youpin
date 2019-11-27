package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.StoreOrder;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;

public interface IStoreOrderDao extends BaseRepository<StoreOrder, Long>, Serializable {
}
