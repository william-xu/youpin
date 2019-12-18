package com.hflw.vasp.admin.modules.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
public class OrderListModel {

    private BigInteger id;
    private String orderNo;
    private Integer status;
    private BigDecimal payAmount;
    private String promoCode;
    private String createTime;
    private String name;
    private String tel;
    private String logisticsNo;

}
