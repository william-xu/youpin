package com.hflw.vasp.eshop.modules.goods.controller;

import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.category.service.CategoryService;
import com.hflw.vasp.eshop.modules.goods.model.GoodsDetailModel;
import com.hflw.vasp.eshop.modules.goods.service.GoodsService;
import com.hflw.vasp.eshop.modules.goodsPicture.service.GoodsPictureService;
import com.hflw.vasp.modules.entity.Goods;
import com.hflw.vasp.modules.entity.GoodsPicture;
import com.hflw.vasp.web.R;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 商品表
 *
 * @author liumh
 * @date 2019-04-01 17:52:54
 */
@RestController
@RequestMapping("/goods")
public class GoodsController extends AbstractController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsPictureService goodsPictureService;

    /**
     * 同类型商品列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        Goods goods = new Goods();
        List<Goods> list = goodsService.search(goods);
        return R.ok().data(list);
    }

    /**
     * 不同类型商品列表
     */
    @RequestMapping("/diffList")
    public R diffList(@RequestParam Map<String, Object> params) {
        Goods goods = new Goods();
        List<Goods> list = goodsService.search(goods);
        Map<String, List<Goods>> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Goods g : list) {
                if (map.containsKey(g.getCategoryCode())) {
                    //map中存在此ParentCode，将数据存放当前key的map中
                    map.get(g.getCategoryCode()).add(g);
                } else {
                    //map中不存在，新建key，用来存放数据
                    List<Goods> tmpList = new ArrayList<>();
                    tmpList.add(g);
                    map.put(g.getCategoryCode(), tmpList);
                }
            }
        }
        return R.ok().data(map);
    }


    /**
     * 商品详情信息
     */
    @RequestMapping("/info")
    public R info(Long id) {
        Goods goods = goodsService.findById(id);
        Map<String, Object> goodsPicParams = new HashMap<>();
        //goodsPicParams.put("enableStatus", Constants.ENABLE_STATUS_EFFECT);
        goodsPicParams.put("goodsId", goods.getId());
        GoodsDetailModel goodsDetailModel = new GoodsDetailModel();
        BeanUtils.copyProperties(goods, goodsDetailModel);
        //针对多张图片
        List<GoodsPicture> pictures = goodsPictureService.search(goodsPicParams);
        List<String> picUrls = new ArrayList<>();
        if (pictures.size() > 1) {
            for (GoodsPicture picture : pictures) {
                picUrls.add(picture.getPicUrl());
            }
        } else {
            picUrls.add(goods.getPicUrl());
        }
        goodsDetailModel.setPicUrls(picUrls);
        return R.ok().data(goodsDetailModel);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody Goods dGoods) {
        //dGoodsService.save(dGoods);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody Goods dGoods) {
        //ValidatorUtils.validateEntity(dGoods);
        //dGoodsService.updateById(dGoods);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        //dGoodsService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
