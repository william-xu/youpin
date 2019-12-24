package com.hflw.vasp.admin.modules.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
public class OrderListModel {

    private BigInteger id;
    private BigInteger userId;
    private String orderNo;
    private String parentOrderNo;
    private Integer status;
    private BigDecimal payAmount;
    private String promoCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private String name;
    private String tel;
    private String logisticsNo;
    private Byte ypcStatus;

}
