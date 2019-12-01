package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Shopcar;
import com.hflw.vasp.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

public interface IShopcarDao extends BaseRepository<Shopcar, Long>, Serializable {

    List<Shopcar> findAllByUserId(Long userId);

    @Query("select sc from Shopcar sc where sc.userId =?1 and sc.goodsId = ?2")
    Shopcar findByUserIdAndGoodsId(Long userId, Long goodsId);

}
