package com.hflw.vasp.eshop.modules.shopcar.model;

import com.hflw.vasp.modules.entity.Shopcar;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车表
 *
 * @author liumh
 * @date 2019-04-02 16:00:37
 */
@Data
public class ShopcarDetail extends Shopcar {

    /**
     * 商品全名称
     */
    private String goodsName;

    /**
     * 商品售价
     */
    private BigDecimal goodsPrice;

    /**
     * 商品主图片
     */
    private String picUrl;

}
