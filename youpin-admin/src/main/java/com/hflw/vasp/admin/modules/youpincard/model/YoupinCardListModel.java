package com.hflw.vasp.admin.modules.youpincard.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
public class YoupinCardListModel {

    private BigInteger id;
    private BigInteger userId;
    private String orderNo;
    private Integer status;
    private Byte ypcStatus;
    private BigDecimal payAmount;
    private String promoCode;

    private String name;
    private String tel;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
