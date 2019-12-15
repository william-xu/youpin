package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 订单商品关系表
 *
 * @author liuyf
 * @date 2019-04-09 15:03:07
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "d_order_goods")
public class OrderGoods extends BaseEntity {

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 订单流水
     */
    @Column(name = "order_no", length = 32)
    private String orderNo;

    /**
     * 商品id
     */
    @Column(name = "goods_Id")
    private Long goodsId;
    /**
     *
     */
    @Column(name = "goods_name", length = 255)
    private String goodsName;
    /**
     * 商品数量
     */
    @Column(name = "goods_num", length = 5)
    private Integer goodsNum;
    /**
     * 商品价格
     */
    @Column(name = "goods_price")
    private BigDecimal goodsPrice;

    /**
     * 支付价格
     */
    @Column(name = "pay_price")
    private BigDecimal payPrice;

    private String picUrl;
}
