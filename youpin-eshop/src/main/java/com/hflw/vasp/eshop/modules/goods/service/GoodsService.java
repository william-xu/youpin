package com.hflw.vasp.eshop.modules.goods.service;

import com.hflw.vasp.eshop.modules.goods.model.GoodsDetailModel;
import com.hflw.vasp.framework.components.PropertiesUtils;
import com.hflw.vasp.modules.dao.IGoodsDao;
import com.hflw.vasp.modules.dao.IGoodsPictureDao;
import com.hflw.vasp.modules.entity.Goods;
import com.hflw.vasp.modules.entity.GoodsPicture;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Service
public class GoodsService {

    @Autowired
    private IGoodsDao goodsDao;

    @Autowired
    private IGoodsPictureDao goodsPictureDao;

    private String getGoodsPicUrl(GoodsPicture gp) {
        String bossImgsUrl = PropertiesUtils.getProperty("boss.imgs.url");
        return bossImgsUrl + gp.getGoodsId() + File.separator + gp.getPicUrl();
    }

    public Goods getById(Long id) {
        Goods goods = goodsDao.getOne(id);
        GoodsPicture gp = goodsPictureDao.findMainByGoodsId(goods.getId());
        goods.setPicUrl(gp.getPicUrl());
        return goods;
    }

    public List<Goods> search(Goods goods) {
        Example<Goods> example = Example.of(goods);
        List<Goods> list = goodsDao.findAll(example);
        for (Goods g : list) {
            GoodsPicture gp = goodsPictureDao.findMainByGoodsId(g.getId());
            if (gp != null) g.setPicUrl(getGoodsPicUrl(gp));
        }
        return list;
    }

    public List<Goods> findByIds(Long[] goodsIds) {
        return goodsDao.findByIds(goodsIds);
    }

    public GoodsDetailModel getInfoById(Long id) {
        Goods goods = goodsDao.getOne(id);
        GoodsDetailModel gdModel = new GoodsDetailModel();
        //多张图片
        List<GoodsPicture> pictureList = goodsPictureDao.findAllByGoodsId(goods.getId());
        List<String> picUrls = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(pictureList)) {
            for (GoodsPicture gp : pictureList) {
                picUrls.add(getGoodsPicUrl(gp));
            }
        }
        BeanUtils.copyProperties(goods, gdModel);
        gdModel.setPicUrls(picUrls);
        return gdModel;
    }

}
