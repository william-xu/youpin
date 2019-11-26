package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface ICategoryDao extends JpaRepository<Category, Long>, Serializable {
}
