package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.YoupinCard;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;

public interface IYoupinCardDao extends BaseRepository<YoupinCard, Long>, Serializable {

    YoupinCard findByUserId(Long userId);

}
