package com.hflw.vasp.eshop.modules.user.service;

import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.modules.dao.IUserDao;
import com.hflw.vasp.modules.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author qurong
 * @Title UserServiceImpl.java
 * @Package com.qurong.service.impl
 * @Description 用户信息
 * @date 2018年10月24日 下午2:37:40
 */
@Service
public class UserService {

    @Autowired
    private IUserDao userDao;

    public Customer getUserByPhone(String phone) {
        return userDao.findByPhone(phone);
    }

    public Long saveOrUpdate(Customer u) {
        if (u.getId() != null) {
            userDao.save(u);
        } else {
            u.setCreateTime(new Date());
            u.setDelFlag(Constants.NOT_DEL);
            u.setEnableStatus(Constants.ENABLE_STATUS_CHECK);
//            userMapper.insertUseGeneratedKeys(u);
        }
        return u.getId();
    }

    public Customer getUserByMiniOpenId(String miniOpenId) {
        return userDao.findByMiniOpenId(miniOpenId);
    }

    public int updateMiniOpenIdByPrimaryKey(Customer u) {
        userDao.save(u);
        return 1;
    }


    public Customer findById(Long id) {
        Optional<Customer> optional = userDao.findById(id);
        return optional.get();
    }

    public void updateByPrimaryKeySelective(Customer user) {
//        userMapper.updateByPrimaryKeySelective(user);
    }

}
