package com.hflw.vasp.system.dao;

import com.hflw.vasp.repository.BaseRepository;
import com.hflw.vasp.system.entity.SysUser;

import java.io.Serializable;

public interface ISysUserDao extends BaseRepository<SysUser, Long>, Serializable {

    SysUser findByUsername(String username);

}
