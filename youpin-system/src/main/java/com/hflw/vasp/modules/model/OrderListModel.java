package com.hflw.vasp.modules.model;

import com.hflw.vasp.modules.entity.Order;
import com.hflw.vasp.modules.entity.OrderAddress;
import com.hflw.vasp.modules.entity.OrderLogistics;
import lombok.AllArgsConstructor;

import javax.xml.crypto.Data;
import java.math.BigDecimal;

@lombok.Data
@AllArgsConstructor
public class OrderListModel {

    private Order order;

    private OrderAddress address;

    private OrderLogistics logistics;

}
