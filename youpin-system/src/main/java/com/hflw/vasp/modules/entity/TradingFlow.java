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
@Table(name = "d_trading_flow")
public class TradingFlow extends BaseEntity {

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 交易流水
     */
    @Column(name = "flow_no", length = 32)
    private String flowNo;

    /**
     * 支付商品名称
     */
    @Column(name = "body", length = 50)
    private String body;

    /**
     * 支付类型：0微信，1、。。。
     */
    @Column(name = "type", length = 2)
    private Integer type;

    /**
     * 支付状态：默认0未支付，1支付成功，2支付失败，预留3支付中
     */
    @Column(name = "status", length = 2)
    private Integer status;

    /**
     * 支付结果：支付机构返回的原因，一般失败才有
     */
    @Column(name = "result", length = 256)
    private String result;

}
