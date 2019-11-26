package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * (由平台分配用户)分配优惠券表
 *
 * @author liumh
 * @date 2019-04-04 18:36:00
 */
@Data
@Entity
@Table(name = "d_store_coupon_config")
@ApiModel("门店优惠券分配表")
public class StoreCouponConfig extends BaseEntity {

    /**
     * 门店id
     */
    private Integer storeId;

    /**
     * 优惠券id
     */
    private Integer couponId;

}
