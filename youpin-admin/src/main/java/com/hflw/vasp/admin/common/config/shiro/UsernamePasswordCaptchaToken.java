package com.hflw.vasp.admin.common.config.shiro;

import lombok.Data;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 拓展登陆验证字段
 */
@Data
public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {

    //验证码字符串
    private String captcha;

    public UsernamePasswordCaptchaToken(String username, char[] password, boolean rememberMe, String captcha) {
        super(username, password, rememberMe);
        this.captcha = captcha;
    }

}