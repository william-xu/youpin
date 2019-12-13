package com.hflw.vasp.eshop.modules.goods.model;

import com.hflw.vasp.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情
 *
 * @author liumh
 * @date 2019-04-01 17:52:54
 */
@Data
public class GoodsDetailModel extends BaseEntity {

    /**
     * 类别code
     */
    private String categoryCode;
    /**
     * 商品全名称
     */
    private String name;
    /**
     * 商品别名
     */
    private String alias;
    /**
     * 商品标签
     */
    private String label;
    /**
     * 成本价
     */
    private BigDecimal costPrice;
    /**
     * 到店价
     */
    private BigDecimal shopPrice;
    /**
     * 零售价
     */
    private BigDecimal retailPrice;
    /**
     * 会员价
     */
    private BigDecimal memberPrice;
    /**
     * 商品主图片
     */
    private List<String> picUrls;
    /**
     * 商品详情
     */
    private String details;

    /**
     * 备注
     */
    private String remark;

}
