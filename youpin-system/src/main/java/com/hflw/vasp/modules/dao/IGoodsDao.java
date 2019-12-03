package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Goods;
import com.hflw.vasp.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

public interface IGoodsDao extends BaseRepository<Goods, Long>, Serializable {

    @Query(value = "select g from Goods g WHERE g.id in(?1)")
    List<Goods> findByIds(Long[] goodsIds);

}
