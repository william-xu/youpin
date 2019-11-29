package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.Customer;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;

public interface ICustomerDao extends BaseRepository<Customer, Long>, Serializable {

    Customer findByPhone(String phone);

    Customer findByWxOpenId(String wxOpenId);

    Customer findByMiniOpenId(String miniOpenId);

}
