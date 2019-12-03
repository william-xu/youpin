package com.hflw.vasp.eshop.modules.goodspicture.service;

import com.hflw.vasp.modules.dao.IGoodsPictureDao;
import com.hflw.vasp.modules.entity.GoodsPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GoodsPictureService {

    @Autowired
    private IGoodsPictureDao goodsPictureDao;

    public List<GoodsPicture> findByGoodsId(Long goodsId) {
        return goodsPictureDao.findAllByGoodsId(goodsId);
    }

    public GoodsPicture findMainByGoodsId(Long goodsId) {
        return goodsPictureDao.findMainByGoodsId(goodsId);
    }

}
