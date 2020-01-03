package com.hflw.vasp.admin.modules.youpincard.model;

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
public class YoupinCardListExport extends BaseRowModel {

    @ExcelProperty(value = "订单编号", index = 0)
    private String orderNo;

    @ExcelProperty(value = "优品卡状态", index = 1)
    private String ypcStatus;

    @ExcelProperty(value = "商户号", index = 2)
    private String promoCode;

    @ExcelProperty(value = "订单支付金额", index = 3)
    private BigDecimal payAmount;

    @ExcelProperty(value = "客户名称", index = 4)
    private String name;

    @ExcelProperty(value = "手机号", index = 5)
    private String tel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty(value = "下单时间", index = 6)
    private Date createTime;

}
