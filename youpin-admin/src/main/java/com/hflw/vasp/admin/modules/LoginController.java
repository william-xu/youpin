package com.hflw.vasp.admin.modules;


import com.hflw.vasp.admin.common.exception.ResultCodeEnum;
import com.hflw.vasp.admin.modules.user.service.UserService;
import com.hflw.vasp.annotation.AccessNoSession;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.framework.components.RedisCacheUtils;
import com.hflw.vasp.system.entity.SysUser;
import com.hflw.vasp.web.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author liuyf
 * @Title UserController.java
 * @Package com.hflw.vasp.controller
 * @Description 登录
 * @date 2019年10月24日 下午2:02:54
 */
@RestController
@Validated
public class LoginController extends AbstractController {

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisCacheUtils redisCacheUtil;

    @Autowired
    private UserService userService;

    @AccessNoSession
    @PostMapping(value = "/login")
    public R login(@NotBlank(message = "用户名不能为空") String username, @NotBlank(message = "密码不能为空") String password, @NotBlank(message = "验证码不能为空") String verifyCode) {
        String kaptcha = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if (kaptcha == null || !StringUtils.equalsIgnoreCase(kaptcha, verifyCode))
            throw BusinessException.create(ResultCodeEnum.VERIFY_CODE_NOT_RIGHT.getCode(), ResultCodeEnum.VERIFY_CODE_NOT_RIGHT.getMsg());

        SysUser user = userService.findByUsername(username);
        if (user == null)
            throw BusinessException.create(ResultCodeEnum.USERNAME_OR_PASSWORD_NOT_RIGHT.getCode(), ResultCodeEnum.USERNAME_OR_PASSWORD_NOT_RIGHT.getMsg());

        String passwd = user.getPassword();
        if (passwd.equals(password))
            throw BusinessException.create(ResultCodeEnum.USERNAME_OR_PASSWORD_NOT_RIGHT.getCode(), ResultCodeEnum.USERNAME_OR_PASSWORD_NOT_RIGHT.getMsg());



        return R.ok();
    }

    @RequestMapping(value = "/index")
    public R index() {
        //登录成功，进入首页
        return R.ok();
    }

    @RequestMapping(value = "/logout")
    public R logout() {
        return R.ok();
    }

}
