package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.GoodsPicture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface IGoodsPictureDao extends JpaRepository<GoodsPicture, Long>, Serializable {
}
