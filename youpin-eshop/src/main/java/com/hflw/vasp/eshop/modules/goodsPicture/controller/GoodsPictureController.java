package com.hflw.vasp.eshop.modules.goodsPicture.controller;

import java.util.Map;
import com.hflw.vasp.eshop.modules.goodsPicture.service.GoodsPictureService;
import com.hflw.vasp.modules.entity.GoodsPicture;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




/**
 * 商品图片表
 *
 * @author Mark
 * @email sunlightcs@gmail.com
 * @date 2019-04-02 10:04:45
 */
@RestController
@RequestMapping("/goodspicture")
public class GoodsPictureController {

    @Autowired
    private GoodsPictureService goodsPictureService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        //PageUtils page = dGoodsPictureService.queryPage(params);

        return R.ok().put("page", "");
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
        //DGoodsPictureEntity dGoodsPicture = dGoodsPictureService.getById(id);

        return R.ok().put("dGoodsPicture", "");
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody GoodsPicture dGoodsPicture){
        //dGoodsPictureService.save(dGoodsPicture);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody GoodsPicture dGoodsPicture){
        //ValidatorUtils.validateEntity(dGoodsPicture);
        //dGoodsPictureService.updateById(dGoodsPicture);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
       // dGoodsPictureService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
