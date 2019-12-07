package com.hflw.vasp.eshop.modules.order.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderYoupinCardModel {

    private String promoCode;

    private BigDecimal originalCost;

    private BigDecimal payAmount;

}
