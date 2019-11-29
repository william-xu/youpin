package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Shopcar;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;
import java.util.List;

public interface IShopcarDao extends BaseRepository<Shopcar, Long>, Serializable {

    List<Shopcar> findAllByUserId(Long userId);

}
