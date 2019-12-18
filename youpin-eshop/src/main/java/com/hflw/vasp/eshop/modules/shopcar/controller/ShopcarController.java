package com.hflw.vasp.eshop.modules.shopcar.controller;

import com.hflw.vasp.annotation.SysLog;
import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.shopcar.model.ShopcarDetail;
import com.hflw.vasp.eshop.modules.shopcar.model.ShopcarModel;
import com.hflw.vasp.eshop.modules.shopcar.service.ShopcarService;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 购物车表
 *
 * @author liuyf
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
    @SysLog
    @RequestMapping("/add")
    public R save(ShopcarModel model) {
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
    @SysLog
    @RequestMapping("/batchAdd")
    public R save(@RequestBody(required = false) List<ShopcarModel> list) {
        shopcarService.saveList(list);
        return R.ok();
    }

}
