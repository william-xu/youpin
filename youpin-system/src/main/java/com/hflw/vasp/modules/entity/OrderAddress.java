package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "d_order_address")
public class OrderAddress extends BaseEntity {

//    @OneToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id")
//    private Order order;

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人手机号
     */
    private String tel;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 快递单号
     */
    @Column(name = "express_no", length = 32)
    private String expressNo;

}
