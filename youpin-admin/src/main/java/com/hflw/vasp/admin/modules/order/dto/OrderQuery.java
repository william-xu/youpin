package com.hflw.vasp.admin.modules.order.dto;

import cn.hutool.db.Page;
import lombok.Data;

@Data
public final class OrderQuery extends Page {

    private String orderNo;

    private Integer orderType;

}
