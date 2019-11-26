package com.hflw.vasp.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "base_provinces")
public class SysProvince implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @ApiModelProperty("主键")
    protected Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    @Column(name = "region_code")
    private String regionCode;

    @Column(name = "region_name")
    private String regionName;

}