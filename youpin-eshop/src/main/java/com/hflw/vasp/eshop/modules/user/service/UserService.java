package com.hflw.vasp.eshop.modules.user.service;

import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.eshop.modules.user.model.UserAddressModel;
import com.hflw.vasp.modules.dao.ICustomerAddressDao;
import com.hflw.vasp.modules.dao.ICustomerDao;
import com.hflw.vasp.modules.entity.Customer;
import com.hflw.vasp.modules.entity.CustomerAddress;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author liuyf
 * @Title UserServiceImpl.java
 * @Package com.hflw.vasp.service.impl
 * @Description 用户信息
 * @date 2019年10月24日 下午2:37:40
 */
@Service
public class UserService {

    @Autowired
    private ICustomerDao userDao;

    @Autowired
    private ICustomerAddressDao customerAddressDao;

    public Customer getUserByPhone(String phone) {
        return userDao.findByPhone(phone);
    }

    public Long saveOrUpdate(Customer u) {
        Date now = new Date();
        if (u.getId() == null) u.setCreateTime(now);
        u.setUpdateTime(now);
        u.setDelFlag(Constants.NOT_DEL);
        u.setEnableStatus(Constants.ENABLE_STATUS_CHECK);
        userDao.save(u);
        return u.getId();
    }

    public void update(Customer u) {
        u.setUpdateTime(new Date());
        userDao.save(u);
    }

    public Customer findByWxOpenId(String wxOpenId) {
        return userDao.findByWxOpenId(wxOpenId);
    }

    public Customer findByMiniOpenId(String miniOpenId) {
        return userDao.findByMiniOpenId(miniOpenId);
    }

    public Customer findById(Long id) {
        Optional<Customer> optional = userDao.findById(id);
        return optional.get();
    }

    public List<CustomerAddress> findAllAddressByUserId(Long userId) {
        return customerAddressDao.findAllByUserId(userId);
    }

    public CustomerAddress getUserAddressById(Long id) {
        Optional<CustomerAddress> optional = customerAddressDao.findById(id);
        return optional.get();
    }

    public void addUserAddress(Long userId, UserAddressModel model) {
        if (model.isDflt()) {
            customerAddressDao.resetByUserId(userId);
        }
        CustomerAddress address = new CustomerAddress();
        BeanUtils.copyProperties(model, address);
        address.setUserId(userId);
        //第一个地址设置为默认
        if (CollectionUtils.isEmpty(customerAddressDao.findAllByUserId(userId))) address.setDflt(true);
        address.setCreateTime(new Date());
        customerAddressDao.save(address);
    }

    public void updateUserAddress(UserAddressModel model) {
        CustomerAddress existAddress = customerAddressDao.getOne(model.getId());

        //重置地址为非默认
        if (model.isDflt()) customerAddressDao.resetByUserId(existAddress.getUserId());

        List<CustomerAddress> list = customerAddressDao.findAllByUserId(existAddress.getUserId());
        if (CollectionUtils.isNotEmpty(list) && list.size() == 1 && model.getId().equals(list.get(0).getId())) {
            //第一个地址设置为默认
            existAddress.setDflt(true);
        }
        existAddress.setName(model.getName());
        existAddress.setTel(model.getTel());
        existAddress.setProvince(model.getProvince());
        existAddress.setProvinceCode(model.getProvinceCode());
        existAddress.setCity(model.getCity());
        existAddress.setCityCode(model.getCityCode());
        existAddress.setArea(model.getArea());
        existAddress.setAreaCode(model.getAreaCode());
        existAddress.setAddress(model.getAddress());
        existAddress.setDflt(model.isDflt());
        existAddress.setUpdateTime(new Date());
        customerAddressDao.save(existAddress);
    }

    public void deleteUserAddressById(Long id) {
        customerAddressDao.deleteById(id);
    }

}
