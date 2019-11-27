package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Goods;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;

public interface IGoodsDao extends BaseRepository<Goods, Long>, Serializable {
}
