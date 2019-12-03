package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.GoodsPicture;
import com.hflw.vasp.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

public interface IGoodsPictureDao extends BaseRepository<GoodsPicture, Long>, Serializable {

    List<GoodsPicture> findAllByGoodsId(Long goodsId);

    @Query("select gp from GoodsPicture gp where gp.goodsId=?1 and gp.isMain = 1")
    GoodsPicture findMainByGoodsId(Long goodsId);

}
