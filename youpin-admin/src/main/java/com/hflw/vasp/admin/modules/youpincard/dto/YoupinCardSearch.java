package com.hflw.vasp.admin.modules.youpincard.dto;

import lombok.Data;

@Data
public final class YoupinCardSearch {

    private String merchantId;

    private String orderNo;

    private String parentOrderNo;

    private String name;

    private String phone;

    private Integer orderStatus;

    /**
     * 优品卡状态：1生效，其他失效
     */
    private Integer ypcStatus;

    private String sDate;
    private String eDate;

}
