package com.hflw.vasp.eshop.modules.areas.controller;

import com.hflw.vasp.eshop.common.exception.ResultCodeEnum;
import com.hflw.vasp.eshop.modules.areas.service.BaseAreasService;
import com.hflw.vasp.system.entity.SysArea;
import com.hflw.vasp.web.R;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author liumh
 * @date 2019-04-03 11:33:49
 */
@RestController
@RequestMapping("base/area")
public class BaseAreasController {

    @Autowired
    private BaseAreasService baseAreasService;

    @RequestMapping("/list")
    public R list(@NotBlank(message = "城市编码不能为空") String cityCode) {
        Map<String, String> params = new HashMap<>();
        params.put("cityCode", cityCode);
        List<SysArea> list = baseAreasService.searchByCityCode(params);
        if (CollectionUtils.isEmpty(list))
            R.error(ResultCodeEnum.AREAS_NOT_EXIST.getCode(), ResultCodeEnum.AREAS_NOT_EXIST.getMsg());
        return R.ok().data(list);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SysArea baseAreas) {
        //baseAreasService.save(baseAreas);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SysArea baseAreas) {
        //ValidatorUtils.validateEntity(baseAreas);
        //baseAreasService.updateById(baseAreas);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        //baseAreasService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
