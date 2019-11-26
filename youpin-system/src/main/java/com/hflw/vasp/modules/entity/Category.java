package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品分类表
 *
 * @author Mark
 * @date 2019-04-01 15:17:15
 */
@Data
@Entity
@Table(name = "d_category")
@ApiModel("商品分类")
public class Category extends BaseEntity {

    /**
     * 类别code
     */
    @Column(name = "code", length = 32)
    private String code;
    /**
     * 类别名称
     */
    @Column(name = "name", length = 64)
    private String name;
    /**
     * 父级id
     */
    @Column(name = "parent_code", length = 32)
    private String parentCode;
    /**
     * 分类层级
     */
    @Column(name = "level", length = 2)
    private Integer level;
    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;
    /**
     * 类别图片url
     */
    @Column(name = "icon")
    private String icon;

}
