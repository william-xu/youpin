package com.hflw.vasp.admin.common.config.shiro;

import com.hflw.vasp.admin.common.exception.IncorrectCaptchaException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class KaptchaFilter extends FormAuthenticationFilter {

    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        UsernamePasswordCaptchaToken token = createToken(request, response);
        try {
            doCaptchaValidate((HttpServletRequest) request, token);
            Subject subject = getSubject(request, response);
            subject.login(token);
            return onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    /**
     * 验证码校验
     *
     * @param request
     * @param token
     */
    protected void doCaptchaValidate(HttpServletRequest request, UsernamePasswordCaptchaToken token) {
        // 从session中获取图形吗字符串
        String captcha = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        // 校验
        if (captcha == null || !captcha.equals(token.getCaptcha())) {
            throw new IncorrectCaptchaException();
        }
    }

    @Override
    protected UsernamePasswordCaptchaToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        boolean rememberMe = isRememberMe(request);
        String captcha = getCaptcha(request);

        return new UsernamePasswordCaptchaToken(username, password.toCharArray(), rememberMe, captcha);
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, DEFAULT_CAPTCHA_PARAM);
    }

    /**
     * 保存异常对象到request
     *
     * @param request
     * @param ae
     */
    @Override
    protected void setFailureAttribute(ServletRequest request, org.apache.shiro.authc.AuthenticationException ae) {
        request.setAttribute(getFailureKeyAttribute(), ae);
    }

}
