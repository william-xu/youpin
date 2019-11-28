package com.hflw.vasp.eshop.common.utils;

import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.modules.entity.Customer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @author liuyf
 * @Title UserUtils.java
 * @Package com.hflw.vasp.credit.sp.utils
 * @Description 获取当前用户工具类
 * @date 2019年7月16日 下午4:35:03
 */
@Component
public class UserUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserUtils.class.getName());

    @Autowired
    HttpSession session;

    public static void putSessionUser(HttpSession session, Customer user) {
        session.setAttribute(Constants.SESSION_LOGIN_USER, user);
    }

    /**
     * 清除当前用户session
     *
     * @param session
     */
    public static void clearSession(HttpSession session) {
        session.removeAttribute(Constants.SESSION_LOGIN_USER);
    }

    /**
     * 从session中获取当前用户
     *
     * @return
     */
    public Customer getSessionUser() {
        Customer user = (Customer) session.getAttribute(Constants.SESSION_LOGIN_USER);
        if (user == null || StringUtils.isBlank(user.getPhone())) {
            return null;
        }
        return user;
    }
}
