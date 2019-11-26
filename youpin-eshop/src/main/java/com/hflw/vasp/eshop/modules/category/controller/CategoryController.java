package com.hflw.vasp.eshop.modules.category.controller;

import com.hflw.vasp.eshop.common.annotation.AuthCheck;
import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.eshop.modules.category.service.CategoryService;
import com.hflw.vasp.modules.entity.Category;
import com.hflw.vasp.validator.ValidatorUtils;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 商品分类表
 *
 * @author liumh
 * @date 2019-04-01 15:17:15
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        //PageUtils page = dCategoryService.queryPage(params);
        return R.ok().put("page", "");
    }


    /**
     * 根据级别获取菜单
     */
    @AuthCheck
    @RequestMapping("/getByLevel")
    public R getByLevel(Integer level) {
        Map<String, Object> params = new HashMap<>();
        params.put("level", level);//分类层级
        params.put("enableStatus", Constants.ENABLE_STATUS_EFFECT);//启用状态 1:启用,2:停用
        List<Category> list = categoryService.search(params);
        return R.ok().put("result", list);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody Category dCategory) {
        //dCategoryService.save(dCategory);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody Category dCategory) {
        ValidatorUtils.validateEntity(dCategory);
        //dCategoryService.updateById(dCategory);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        //dCategoryService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
