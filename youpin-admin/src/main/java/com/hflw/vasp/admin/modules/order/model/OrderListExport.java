package com.hflw.vasp.admin.modules.order.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单列表导出
 */
@Data
public class OrderListExport extends BaseRowModel {

    @ExcelProperty(value = "订单编号", index = 0)
    private String orderNo;

    @ExcelProperty(value = "优品卡编号", index = 1)
    private String parentOrderNo;

    @ExcelProperty(value = "订单状态", index = 2)
    private String status;

    @ExcelProperty(value = "订单支付金额", index = 3)
    private BigDecimal payAmount;

    @ExcelProperty(value = "收货人姓名", index = 4)
    private String name;

    @ExcelProperty(value = "收货人手机号", index = 5)
    private String tel;

    @ExcelProperty(value = "发货单号", index = 6)
    private String logisticsNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(value = "下单时间", index = 7)
    private Date createTime;

}
