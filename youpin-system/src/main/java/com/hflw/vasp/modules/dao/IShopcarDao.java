package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Shopcar;
import com.hflw.vasp.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

public interface IShopcarDao extends BaseRepository<Shopcar, Long>, Serializable {

    List<Shopcar> findAllByUserId(Long userId);

    Shopcar findByUserIdAndGoodsId(Long userId, Long goodsId);

    @Modifying
    @Transactional
    void deleteByUserIdAndAndGoodsId(Long userId, Long goodsId);

}
