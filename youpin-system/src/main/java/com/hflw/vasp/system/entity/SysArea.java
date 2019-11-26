package com.hflw.vasp.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "base_areas")
public class SysArea implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @ApiModelProperty("主键")
    protected Long id;

    /**
     * 地市code
     */
    private String code;

    /**
     * 地市名称
     */
    private String name;

    /**
     * 地市code
     */
    @Column(name = "city_code")
    private String cityCode;

    /**
     * 省份code
     */
    @Column(name = "province_code")
    private String provinceCode;

}