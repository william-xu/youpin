package com.hflw.vasp.eshop.modules.province.service;

import com.hflw.vasp.framework.components.RedisCacheUtils;
import com.hflw.vasp.framework.constant.ConstantKeys;
import com.hflw.vasp.system.dao.ISysProvinceDao;
import com.hflw.vasp.system.entity.SysProvince;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class BaseProvincesService {

    @Resource
    private ISysProvinceDao provinceDao;

    @Autowired
    private RedisCacheUtils redisCacheUtil;

    public List<SysProvince> list() {
        List<SysProvince> list = redisCacheUtil.getCacheList(ConstantKeys.REDIS_PROVINCES_KEY);
        //排序
        Collections.sort(list, Comparator.comparing(SysProvince::getCode));
        if (CollectionUtils.isEmpty(list)) list = provinceDao.findAll();
        return list;
    }
}
