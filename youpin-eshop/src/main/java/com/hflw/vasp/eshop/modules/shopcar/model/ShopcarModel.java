package com.hflw.vasp.eshop.modules.shopcar.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 购物车表
 *
 * @author liuyf
 * @date 2019-04-02 16:00:37
 */
@Data
public class ShopcarModel implements Serializable {

    private static final long serialVersionUID = 7230477939668640627L;

    private Long userId;

    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    private Long goodsId;

    /**
     * 商品数量
     */
    @NotNull(message = "商品数量必须在0到10之间")
    @Range(min = 1, max = 10, message = "商品数量必须在0到10之间")
    private Integer goodsNum;

}
