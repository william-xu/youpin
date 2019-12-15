package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * 订单表
 *
 * @author liuyf
 * @date 2019-04-09 14:21:28
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "d_order")
public class Order extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 订单流水
     */
    @Column(name = "order_no", length = 32)
    private String orderNo;

    /**
     * 父订单流水
     */
    @Column(name = "parent_order_no", length = 32)
    private String parentOrderNo;

    /**
     * 订单类型：0商品订单，1优品卡订单
     */
    @Column(name = "type", length = 2)
    private int type;

    @Transient
    private BigDecimal totalAmount;

    /**
     * 订单总额
     */
    @Column(name = "pay_amount", length = 20)
    private BigDecimal payAmount;
    /**
     * 订单优惠金额
     */
    @Column(name = "discount_amount", length = 20)
    private BigDecimal discountAmount;

    /**
     * 订单利润
     */
    @Column(name = "profit", length = 20)
    private BigDecimal profit;

    /**
     * 订单状态：0已下单，1已支付，2退款
     */
    @Column(name = "status", length = 2)
    private Integer status;

}
