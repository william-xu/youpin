package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 由（平台）派发/（用户）领用优惠券表
 *
 * @author liuyf
 * @date 2019-04-04 18:47:32
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "d_coupon_recipients")
@ApiModel("优惠券表")
public class CouponRecipients extends BaseEntity {
    /**
     * 门店id
     */
    @Column(name = "user_id", length = 32)
    private Integer userId;
    /**
     * 优惠券id
     */
    @Column(name = "coupon_id", length = 32)
    private Integer couponId;

    @Column(name = "coupon_title", length = 50)
    private String couponTitle;
    /**
     * 商品id
     */
    @Column(name = "goods_id", length = 32)
    private Integer goodsId;
    /**
     * 领券时劵成本价
     */
    @Column(name = "cost_amount", length = 20)
    private BigDecimal costAmount;
    /**
     * 领券时劵抵用金额
     */
    @Column(name = "used_amount", length = 20)
    private BigDecimal usedAmount;
    /**
     * 领券时抵用券数量
     */
    @Column(name = "used_quantity", length = 5)
    private Integer usedQuantity;
    /**
     * 金融订单编号
     */
    @Column(name = "finance_order_no", length = 32)
    private String financeOrderNo;
    /**
     * 车架号
     */
    @Column(name = "vin_no", length = 32)
    private String vinNo;

    /**
     * 券使用状态：1未使用，2已使用
     */
    @Column(name = "used_flag", length = 2)
    private Integer usedFlag;

    @Column(name = "used_threshold", length = 2)
    private Integer usedThreshold;

}
