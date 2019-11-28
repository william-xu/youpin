package com.hflw.vasp.eshop.modules.category.service;

import com.hflw.vasp.modules.dao.ICategoryDao;
import com.hflw.vasp.modules.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private ICategoryDao categoryDao;


    public List<Category> list(Category category) {
        Example<Category> example = Example.of(category);
        return categoryDao.findAll(example);
    }

}
