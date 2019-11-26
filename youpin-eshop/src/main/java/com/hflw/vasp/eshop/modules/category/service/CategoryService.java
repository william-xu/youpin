package com.hflw.vasp.eshop.modules.category.service;

import com.hflw.vasp.modules.dao.ICategoryDao;
import com.hflw.vasp.modules.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CategoryService {

    @Autowired
    private ICategoryDao categoryDao;


    public List<Category> search(Map<String, Object> params) {
        return categoryDao.findAll();
    }

}
