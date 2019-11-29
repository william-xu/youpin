package com.hflw.vasp.eshop.modules.cities.service;

import com.hflw.vasp.framework.components.RedisCacheUtils;
import com.hflw.vasp.framework.constant.CacheConstants;
import com.hflw.vasp.system.dao.IBaseCitiesDao;
import com.hflw.vasp.system.entity.SysCity;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


@Service
public class BaseCitiesService {

    @Resource
    private IBaseCitiesDao citiesDao;

    @Autowired
    private RedisCacheUtils redisCacheUtil;

    public List<SysCity> list(Map<String, String> params) {
        String provinceCode = params.get("provinceCode");
        List<SysCity> list = redisCacheUtil.getListByPrex(CacheConstants.REDIS_CITY_KEY + provinceCode);
        Collections.sort(list, Comparator.comparing(SysCity::getCode));

        SysCity city = new SysCity();
        city.setProvinceCode(provinceCode);
        Example<SysCity> example = Example.of(city);
        if (CollectionUtils.isEmpty(list)) list = citiesDao.findAll(example);
        return list;
    }
}
