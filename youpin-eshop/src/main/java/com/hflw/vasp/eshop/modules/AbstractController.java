package com.hflw.vasp.eshop.modules;

import com.hflw.vasp.controller.BaseController;
import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.modules.entity.Customer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author liuyf
 * @Title BaseController.java
 * @Package com.hflw.vasp.controller
 * @Description
 * @date 2019年10月24日 下午2:24:00
 */
@Controller
public class AbstractController extends BaseController {

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpSession session;

    /**
     * 从session中获取当前用户
     *
     * @return
     */
    @Override
    public Customer getSessionUser() {
        Customer user = (Customer) session.getAttribute(Constants.SESSION_LOGIN_USER);
        if (user == null || StringUtils.isBlank(user.getPhone())) {
            return null;
        }
        return user;
    }

    @Override
    public String getAccount() {
        Customer user = getSessionUser();
        if (user != null) {
            return user.getPhone();
        }
        return null;
    }

    @Override
    public Long getUserId() {
        Customer user = getSessionUser();
        if (user != null) {
            return user.getId();
        }
        return null;
    }

}
