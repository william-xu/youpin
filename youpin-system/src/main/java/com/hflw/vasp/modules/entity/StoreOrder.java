package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 门店订单表
 *
 * @author liumh
 * @date 2019-04-09 14:21:28
 */
@Data
@Entity
@Table(name = "d_store_order")
public class StoreOrder extends BaseEntity {

    /**
     * 门店id
     */
    @Column(name = "store_user_id")
    private Integer userId;
    /**
     * 订单流水
     */
    @Column(name = "order_no", length = 32)
    private String orderNo;
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
     * 金融订单编号
     */
	/*@Column(name = "finance_order_no", length = 32)
	private String financeOrderNo;
	*//**
     * 车架号
     *//*
	@Column(name = "vin_no", length = 32)
	private String vinNo;*/
    /**
     * 订单状态：1已下单，2已安装
     */
    @Column(name = "status", length = 2)
    private Integer status;

    @Override
    public String toString() {
        return "StoreOrder{" +
                "userId=" + userId +
                ", orderNo='" + orderNo + '\'' +
                ", payAmount=" + payAmount +
                ", discountAmount=" + discountAmount +
                ", profit=" + profit +
                ", status='" + status + '\'' +
                '}';
    }
}
