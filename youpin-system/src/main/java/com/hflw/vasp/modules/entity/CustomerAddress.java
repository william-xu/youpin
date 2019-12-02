package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "d_customer_address")
@ApiModel("客户地址")
public class CustomerAddress extends BaseEntity {

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人手机号
     */
    private String tel;

    /**
     * 省份code
     */
    @Column(name = "province", length = 30)
    private String province;

    /**
     * 省份code
     */
    @Column(name = "province_code", length = 20)
    private String provinceCode;

    /**
     * 地市code
     */
    @Column(name = "city", length = 30)
    private String city;

    /**
     * 地市code
     */
    @Column(name = "city_code", length = 20)
    private String cityCode;

    /**
     * 县区
     */
    @Column(name = "area", length = 30)
    private String area;

    /**
     * 县区code
     */
    @Column(name = "area_code", length = 20)
    private String areaCode;

    /**
     * 详细地址
     */
    @Column(name = "address", length = 256)
    private String address;

    /**
     * 是否默认：0否，1是
     */
    @Column(name = "is_dflt", length = 2)
    private boolean isDflt;

}
