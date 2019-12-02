package com.hflw.vasp.eshop.modules.user.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddressModel implements Serializable {

    private static final long serialVersionUID = 7230477939668640627L;

    private Long id;

    private String name;

    private String tel;

    private String province;

    private String provinceCode;

    private String city;

    private String cityCode;

    private String area;

    private String areaCode;

    private String address;

    private boolean isDflt;

}
