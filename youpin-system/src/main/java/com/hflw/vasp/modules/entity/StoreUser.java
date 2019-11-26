package com.hflw.vasp.modules.entity;

import com.hflw.vasp.entity.BaseSysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "d_store_user")
@ApiModel("门店用户")
public class StoreUser extends BaseSysUser {

    @ApiModelProperty("手机号")
    @Column(name = "phone", length = 11)
    private String phone;

    @ApiModelProperty("微信公众号openid")
    @Column(name = "wx_openid", length = 64)
    private String wxOpenId;

    @ApiModelProperty("关联微信小程序openid")
    @Column(name = "mini_openid", length = 64)
    private String miniOpenId;

    @Override
    public String toString() {
        return "StoreUser{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", wxOpenId='" + wxOpenId + '\'' +
                ", miniOpenId='" + miniOpenId + '\'' +
                '}';
    }

}