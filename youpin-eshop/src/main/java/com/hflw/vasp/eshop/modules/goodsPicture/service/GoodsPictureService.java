package com.hflw.vasp.eshop.modules.goodsPicture.service;

import com.hflw.vasp.modules.dao.IGoodsPictureDao;
import com.hflw.vasp.modules.entity.GoodsPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class GoodsPictureService {

    @Autowired
    private IGoodsPictureDao goodsPictureDao;

    public List<GoodsPicture> search(Map<String, Object> params) {
        GoodsPicture picture = new GoodsPicture();
        picture.setGoodsId((Long) params.get("goodsId"));
        Example<GoodsPicture> example = Example.of(picture);
        return goodsPictureDao.findAll(example);
    }

    public List<GoodsPicture> findByGoodsId(Long goodsId) {
        return null;
    }

}
