package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.CustomerAddress;
import com.hflw.vasp.repository.BaseRepository;

import java.io.Serializable;
import java.util.List;

public interface ICustomerAddressDao extends BaseRepository<CustomerAddress, Long>, Serializable {

    List<CustomerAddress> findAllByUserId(Long userId);

}
