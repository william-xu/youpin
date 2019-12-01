package com.hflw.vasp.eshop.modules.order.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 购物车表
 *
 * @author liumh
 * @date 2019-04-02 16:00:37
 */
@Data
public class OrderGoodsModel implements Serializable {

    private static final long serialVersionUID = 7230477939668640627L;

    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    private Integer goodsId;

    @NotEmpty(message = "商品id不能为空")
    private String name;

    /**
     * 商品数量
     */
    @NotNull(message = "商品数量不能为空")
    @Range(min = 1, max = 10, message = "商品数量必须在0到10之间")
    private Integer goodsNum;

}
