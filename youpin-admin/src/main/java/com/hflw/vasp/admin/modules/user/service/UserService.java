package com.hflw.vasp.admin.modules.user.service;

import com.hflw.vasp.enums.SysContants;
import com.hflw.vasp.system.dao.ISysUserDao;
import com.hflw.vasp.system.entity.SysUser;
import com.hflw.vasp.utils.StringUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

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

    @Autowired
    private HashedCredentialsMatcher hcm;

    public SysUser findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public SysUser addUser(SysUser user) {

        String password = user.getPassword();

        // uuid获取随机字符串，作为盐值。
        String salt = StringUtils.generateRandomCode(false, 4);
        user.setSalt(salt);
        //add Salt to password
        //user.getCredentialsSalt我没有直接使用盐，而是将用户名和盐作为密码加密的最终盐值
        SimpleHash hash = new SimpleHash(hcm.getHashAlgorithmName(), password, user.getSalt(), hcm.getHashIterations());
        //重新赋值
        user.setPassword(hash.toString());
        user.setEnableStatus(SysContants.EnableStatus.enable.getCode());
        user.setCreateTime(new Date());
        userDao.save(user);
        return user;
    }

}
