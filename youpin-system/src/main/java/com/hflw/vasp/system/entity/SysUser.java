package com.hflw.vasp.system.entity;

import com.hflw.vasp.entity.BaseSysUser;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "d_user")
public class SysUser extends BaseSysUser {

    /**
     * 加密盐
     */
    private String salt;

}
