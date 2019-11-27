package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Category;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;

public interface ICategoryDao extends BaseRepository<Category, Long>, Serializable {
}
