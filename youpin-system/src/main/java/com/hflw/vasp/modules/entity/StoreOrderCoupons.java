package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 订单用券关系表
 *
 * @author liumh
 * @date 2019-04-09 14:52:40
 */
@Data
@Entity
@Table(name = "d_store_order_coupons")
public class StoreOrderCoupons extends BaseEntity {

    /**
     * 订单流水
     */
    @Column(name = "order_no", length = 32)
    private String orderNo;

    /**
     * 用券id
     */
    @Column(name = "coupon_recipients_id")
    private Integer couponRecipientsId;

}
