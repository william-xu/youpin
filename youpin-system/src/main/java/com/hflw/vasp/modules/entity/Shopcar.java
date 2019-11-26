package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 购物车表
 *
 * @author liumh
 * @date 2019-04-02 16:00:37
 */
@Data
@Entity
@Table(name = "d_shopcar")
@ApiModel("购物车")
public class Shopcar extends BaseEntity {
    /**
     * 用户id
     */
    @Column(name = "user_id", length = 20)
    private Long userId;
    /**
     * 商品id
     */
    @Column(name = "goods_id", length = 11)
    private Long goodsId;

    /**
     * 商品数量
     */
    @Column(name = "goods_num", length = 4)
    private Integer goodsNum;

}
