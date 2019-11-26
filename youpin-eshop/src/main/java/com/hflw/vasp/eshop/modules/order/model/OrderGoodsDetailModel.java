package com.hflw.vasp.eshop.modules.order.model;

import java.math.BigDecimal;

/**
 * 订单商品关系表
 *
 * @author liumh
 * @date 2019-04-09 15:03:07
 */
public class OrderGoodsDetailModel  {
    private Integer id;
    /**
     * 订单流水
     */
    private String orderNo;
    /**
     * 商品id
     */
    private Integer goodsId;
    /**
     *
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private Integer goodsNum;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 实际支付金额
     */
    private BigDecimal costPrice;

    /**
     * 领券时抵用券数量
     */
    private Integer usedQuantity;

    /**
     * 商品主图片
     */
    private String picUrl;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getUsedQuantity() {
        return usedQuantity;
    }

    public void setUsedQuantity(Integer usedQuantity) {
        this.usedQuantity = usedQuantity;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
