package com.hflw.vasp.modules.model;

import lombok.AllArgsConstructor;

import javax.xml.crypto.Data;
import java.math.BigDecimal;

@lombok.Data
@AllArgsConstructor
public class OrderListModel {

    public OrderListModel() {
    }

    private Long orderId;
    private String orderNo;
    private BigDecimal payAmount;
    private Long status;
    private Long ypcStatus;
    private Data createTime;
    private String buyer;
    private String phone;


}
