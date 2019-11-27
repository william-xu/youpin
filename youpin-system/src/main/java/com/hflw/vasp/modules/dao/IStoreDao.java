package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Store;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;

public interface IStoreDao extends BaseRepository<Store, Long>, Serializable {
}