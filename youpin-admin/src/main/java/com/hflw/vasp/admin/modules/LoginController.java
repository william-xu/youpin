package com.hflw.vasp.admin.modules;


import com.hflw.vasp.admin.common.config.shiro.UsernamePasswordCaptchaToken;
import com.hflw.vasp.web.R;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author liuyf
 * @Title UserController.java
 * @Package com.hflw.vasp.controller
 * @Description 登录
 * @date 2019年10月24日 下午2:02:54
 */
@Validated
@RestController
public class LoginController extends AbstractController {

    @Autowired
    private HashedCredentialsMatcher hcm;

    @PostMapping(value = "/login")
    @ResponseBody
    public R login(@NotBlank(message = "用户名不能为空") String username,
                   @NotBlank(message = "密码不能为空") String password,
                   @NotBlank(message = "验证码不能为空") String captcha) {
//        UsernamePasswordCaptchaToken token = new UsernamePasswordCaptchaToken(username, password.toCharArray(), true, captcha);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        SecurityUtils.getSubject().login(token);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        return R.ok().data(session.getId());
    }

    @RequestMapping(value = "/logout")
    public R logout() {
        return R.ok();
    }

    @RequestMapping(value = "/index")
    public R index() {
        //登录成功，进入首页
        return R.ok();
    }

}
