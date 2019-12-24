package com.hflw.vasp.admin.modules.order.dto;

import cn.hutool.db.Page;
import lombok.Data;

@Data
public final class OrderSearch extends Page {

    private String merchantId;

    private Integer orderType;

    private String orderNo;

    private String parentOrderNo;

    private Integer orderStatus;

    /**
     * 优品卡状态：1生效，其他失效
     */
    private Integer ypcStatus;

    private String name;

    private String sDate;
    private String eDate;

}
