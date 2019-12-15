package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 订单用券关系表
 *
 * @author liuyf
 * @date 2019-04-09 14:52:40
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "d_order_coupons")
public class OrderCoupons extends BaseEntity {

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
