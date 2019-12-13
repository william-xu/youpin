package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 物流快递
 */
@Data
@Entity
@Table(name = "d_order_logistics")
public class OrderLogistics extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 物流公司
     */
    @Column(name = "company", length = 32)
    private String company;

    /**
     * 物流单号
     */
    @Column(name = "number", length = 32)
    private String number;

}
