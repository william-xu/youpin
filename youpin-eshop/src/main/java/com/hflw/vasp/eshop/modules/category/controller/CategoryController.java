package com.hflw.vasp.eshop.modules.category.controller;

import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.eshop.modules.category.service.CategoryService;
import com.hflw.vasp.modules.entity.Category;
import com.hflw.vasp.validator.ValidatorUtils;
import com.hflw.vasp.web.R;
import org.springframework.beans.BeanUtils;
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
 * @author liuyf
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
        Category category = new Category();
        BeanUtils.copyProperties(params, category);
        List<Category> list = categoryService.list(category);
        return R.ok().data(list);
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
