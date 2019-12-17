package com.hflw.vasp.eshop.modules.goods.controller;

import com.hflw.vasp.enums.SysContants;
import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.category.service.CategoryService;
import com.hflw.vasp.eshop.modules.goods.model.GoodsDetailModel;
import com.hflw.vasp.eshop.modules.goods.service.GoodsService;
import com.hflw.vasp.modules.entity.Goods;
import com.hflw.vasp.web.R;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author liuyf
 * @date 2019-04-01 17:52:54
 */
@RestController
@RequestMapping("/goods")
public class GoodsController extends AbstractController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private GoodsService goodsService;

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
    @RequestMapping("/difflist")
    public R diffList(@RequestParam Map<String, Object> params) {
        Goods goods = new Goods();
        goods.setEnableStatus(SysContants.EnableStatus.enable.getCode());
        goods.setDelFlag(SysContants.DeleteStatus.normal.getCode());

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
        GoodsDetailModel gdModel = goodsService.getInfoById(id);
        return R.ok().data(gdModel);
    }

}
