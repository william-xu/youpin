package com.hflw.vasp.modules.dao;

import com.hflw.vasp.modules.entity.OrderLogistics;
import com.hflw.vasp.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.io.Serializable;

public interface IOrderLogisticsDao extends BaseRepository<OrderLogistics, Long>, Serializable {

    @Query("select ol from OrderLogistics ol where ol.delFlag = 0 and ol.orderId=?1 order by ol.id desc")
    OrderLogistics findByOrderId(Long orderId);

    @Transactional
    @Modifying
    @Query("update OrderLogistics ol set ol.delFlag = -1 where ol.id=?1")
    void logicDeleteById(Long id);

}
