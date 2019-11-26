package com.hflw.vasp.eshop.modules;

import com.hflw.vasp.controller.BaseController;
import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.modules.entity.StoreUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;

/**
 * @author qurong
 * @Title BaseController.java
 * @Package com.qurong.controller
 * @Description
 * @date 2018年10月24日 下午2:24:00
 */
@Controller
public class AbstractController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected HttpSession session;

    /**
     * 从session中获取当前用户
     *
     * @return
     */
    public StoreUser getSessionUser() {
        StoreUser user = (StoreUser) session.getAttribute(Constants.SESSION_LOGIN_USER);
        if (user == null || StringUtils.isBlank(user.getPhone())) {
            return null;
        }
        return user;
    }

    public String getUserName() {
        StoreUser user = getSessionUser();
        if (user != null) {
            return user.getPhone();
        }
        return null;
    }

    public Long getUserId() {
        StoreUser user = getSessionUser();
        if (user != null) {
            return user.getId();
        }
        return null;
    }

}
