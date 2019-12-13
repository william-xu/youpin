package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * 商品表
 *
 * @author liumh
 * @date 2019-04-01 17:52:54
 */
@Data
@Entity
@Table(name = "d_goods")
@ApiModel("商品表")
public class Goods extends BaseEntity {

    /**
     * 类别code
     */
    @Column(name = "category_code", length = 32)
    private String categoryCode;
    /**
     * 商品全名称
     */
    @Column(name = "name", length = 255)
    private String name;
    /**
     * 商品别名
     */
    @Column(name = "alias", length = 100)
    private String alias;
    /**
     * 商品标签
     */
    @Column(name = "label", length = 1024)
    private String label;
    /**
     * 成本价
     */
    @Column(name = "cost_price", length = 20)
    private BigDecimal costPrice;
    /**
     * 到店价
     */
    @Column(name = "shop_price", length = 20)
    private BigDecimal shopPrice;
    /**
     * 零售价
     */
    @Column(name = "retail_price", length = 20)
    private BigDecimal retailPrice;

    /**
     * 会员价
     */
    @Transient
    private BigDecimal memberPrice;

    /**
     * 商品主图片
     */
    @Column(name = "pic_url", length = 255)
    private String picUrl;
    /**
     * 商品详情
     */
    @Column(name = "details", length = 1024)
    private String details;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

}
