package com.hflw.vasp.eshop.modules.shopcar.controller;

import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.shopcar.model.ShopcarDetail;
import com.hflw.vasp.eshop.modules.shopcar.model.ShopcarModel;
import com.hflw.vasp.eshop.modules.shopcar.service.ShopcarService;
import com.hflw.vasp.web.Pagination;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 购物车表
 *
 * @author liumh
 * @date 2019-04-02 16:00:37
 */
@RestController
@RequestMapping("/shopcar")
public class ShopcarController extends AbstractController {

    @Autowired
    private ShopcarService shopcarService;

    /**
     * 购物车列表
     */
    @RequestMapping("/list")
    public R list() {
        List<ShopcarDetail> list = shopcarService.list(getUserId());
        return R.ok().data(list);
    }

    /**
     * 购物车保存商品
     */
    @RequestMapping("/add")
    public R save(ShopcarModel model) {
        logger.info(model.toString());
        model.setUserId(getUserId());
        shopcarService.addToShopcar(model);
        return R.ok();
    }

    /**
     * 修改商品数量
     */
    @RequestMapping("/update")
    public R update(ShopcarModel model) {
        model.setUserId(getUserId());
        shopcarService.updateShopcar(model);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(Long[] ids) {
        shopcarService.delFromShopcar(ids);
        return R.ok();
    }

    /**
     * 购物车批量添加商品
     */
    @RequestMapping("/batchAdd")
    public R save(@RequestBody(required = false) List<ShopcarModel> list) {
        logger.info(list.toString());
        shopcarService.saveList(list);
        return R.ok();
    }

    /**
     * 购物车添加缓存商品
     */
    @RequestMapping("/reLoginSave")
    public R reLoginSave(@RequestBody(required = false) List<ShopcarModel> list, Pagination pagination) {
        logger.info(list.toString());
        shopcarService.saveList(list);
        Map<String, Object> shopParams = new HashMap<>();
        shopParams.put("userId", getUserId());
        shopParams.put("enableStatus", Constants.ENABLE_STATUS_EFFECT);
//        PageHelper.startPage(pagination.getCurrentPage(), pagination.getPageSize());
//        List<ShopcarDetail> shopcarDetailList = shopcarService.searchShopcarDetail(shopParams);
//        long total = ((Page<ShopcarDetail>) shopcarDetailList).getTotal();
//        return R.ok().putPageData(shopcarDetailList, total);
        return null;
    }

}
