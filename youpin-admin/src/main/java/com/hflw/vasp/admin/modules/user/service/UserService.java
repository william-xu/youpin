package com.hflw.vasp.admin.modules.user.service;

import com.hflw.vasp.system.dao.ISysUserDao;
import com.hflw.vasp.system.entity.SysUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liuyf
 * @Title UserServiceImpl.java
 * @Package com.hflw.vasp.service.impl
 * @Description 用户信息
 * @date 2019年10月24日 下午2:37:40
 */
@Service
public class UserService {

    @Resource
    private ISysUserDao userDao;

    public SysUser findByUsername(String username) {
        return userDao.findByUsername(username);
    }

}
