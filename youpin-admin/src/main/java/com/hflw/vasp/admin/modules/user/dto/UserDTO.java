package com.hflw.vasp.admin.modules.user.dto;

import com.google.common.base.Converter;
import com.hflw.vasp.system.entity.SysUser;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {

    @NotBlank
    private String username;

    private String realname;

    @NotBlank
    private String password;

    @NotBlank
    private String phone;

    private String remark;

    private Long[] roleIds;

    public SysUser convertTo() {
        DTOConverter convert = new DTOConverter();
        return convert.convert(this);
    }

    public UserDTO convertFor(SysUser user) {
        DTOConverter convert = new DTOConverter();
        return convert.reverse().convert(user);
    }

    private static class DTOConverter extends Converter<UserDTO, SysUser> {
        @Override
        protected SysUser doForward(UserDTO source) {
            SysUser target = new SysUser();
            BeanUtils.copyProperties(source, target);
            return target;
        }

        @Override
        protected UserDTO doBackward(SysUser source) {
            UserDTO target = new UserDTO();
            BeanUtils.copyProperties(source, target);
            return target;
        }
    }

}
