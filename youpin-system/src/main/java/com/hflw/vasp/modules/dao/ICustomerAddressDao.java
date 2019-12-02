package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.CustomerAddress;
import com.hflw.vasp.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

public interface ICustomerAddressDao extends BaseRepository<CustomerAddress, Long>, Serializable {

    List<CustomerAddress> findAllByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("update CustomerAddress ca set ca.isDflt = false where ca.userId=?1")
    void resetByUserId(Long userId);

}
