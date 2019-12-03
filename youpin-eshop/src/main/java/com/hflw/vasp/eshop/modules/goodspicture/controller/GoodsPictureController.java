package com.hflw.vasp.eshop.modules.goodspicture.controller;

import com.hflw.vasp.eshop.modules.goodspicture.service.GoodsPictureService;
import com.hflw.vasp.modules.entity.GoodsPicture;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 商品图片表
 *
 * @author Mark
 * @date 2019-04-02 10:04:45
 */
@RestController
@RequestMapping("/goodspicture")
public class GoodsPictureController {

    @Autowired
    private GoodsPictureService goodsPictureService;

    /**
     * 商品图片列表
     */
    @RequestMapping("/list")
    public R listByGoodsId(Long goodsId) {
        List<GoodsPicture> list = goodsPictureService.findByGoodsId(goodsId);
        return R.ok().data(list);
    }

}
