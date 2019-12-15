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

    public SysUser convertToUser() {
        UserDTOConvert userDTOConvert = new UserDTOConvert();
        SysUser convert = userDTOConvert.convert(this);
        return convert;
    }

    public UserDTO convertFor(SysUser user) {
        UserDTOConvert userDTOConvert = new UserDTOConvert();
        UserDTO convert = userDTOConvert.reverse().convert(user);
        return convert;
    }

    private static class UserDTOConvert extends Converter<UserDTO, SysUser> {
        @Override
        protected SysUser doForward(UserDTO userDTO) {
            SysUser user = new SysUser();
            BeanUtils.copyProperties(userDTO, user);
            return user;
        }

        @Override
        protected UserDTO doBackward(SysUser user) {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }
    }

}
