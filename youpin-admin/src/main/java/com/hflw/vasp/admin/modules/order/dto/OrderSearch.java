package com.hflw.vasp.admin.modules.order.dto;

import lombok.Data;

@Data
public final class OrderSearch {

    private String orderNo;

    private String parentOrderNo;

    private String phone;

    private Integer orderStatus;

    private String name;

    private String sDate;
    private String eDate;

}
