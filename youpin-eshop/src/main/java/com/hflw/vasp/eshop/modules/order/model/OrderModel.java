package com.hflw.vasp.eshop.modules.order.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Arrays;

@Data
public class OrderModel {

    private Long[] goodsIds;

    private Integer[] goodsNums;

    private BigDecimal payAmount;

    private Long addressId;

    @Override
    public String toString() {
        return "OrderModel{" +
                "goodsIds=" + Arrays.toString(goodsIds) +
                ", goodsNums=" + Arrays.toString(goodsNums) +
                ", payAmount=" + payAmount +
                ", addressId=" + addressId +
                '}';
    }
}
