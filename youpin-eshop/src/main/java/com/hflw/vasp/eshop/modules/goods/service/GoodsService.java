package com.hflw.vasp.eshop.modules.goods.service;

import com.hflw.vasp.framework.components.PropertiesUtils;
import com.hflw.vasp.modules.dao.IGoodsDao;
import com.hflw.vasp.modules.dao.IGoodsPictureDao;
import com.hflw.vasp.modules.entity.Goods;
import com.hflw.vasp.modules.entity.GoodsPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;


@Service
public class GoodsService {

    @Autowired
    private IGoodsDao goodsDao;

    @Autowired
    private IGoodsPictureDao goodsPictureDao;

    public Goods findById(Long id) {
        Optional<Goods> optional = goodsDao.findById(id);
        return optional.orElse(null);
    }

    public List<Goods> search(Goods goods) {
        String bossImgsUrl = PropertiesUtils.getProperty("boss.imgs.url");

        Example<Goods> example = Example.of(goods);
        List<Goods> list = goodsDao.findAll(example);
        for (Goods g : list) {
            GoodsPicture gp = goodsPictureDao.findMainByGoodsId(g.getId());
            if (gp != null) g.setPicUrl(bossImgsUrl + g.getId() + File.separator + gp.getPicUrl());
        }
        return list;
    }

    public List<Goods> findByIds(Long[] goodsIds) {
        return goodsDao.findByIds(goodsIds);
    }

}
