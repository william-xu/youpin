package com.hflw.vasp.admin.modules;

import com.hflw.vasp.admin.common.constant.Constants;
import com.hflw.vasp.controller.BaseController;
import com.hflw.vasp.system.entity.SysUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public Logger logger = LoggerFactory.getLogger(getClass());

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
    public SysUser getSessionUser() {
        SysUser user = (SysUser) session.getAttribute(Constants.SESSION_LOGIN_USER);
        if (user == null || StringUtils.isBlank(user.getPhone())) {
            return null;
        }
        return user;
    }

    @Override
    public String getAccount() {
        SysUser user = getSessionUser();
        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    @Override
    public Long getUserId() {
        SysUser user = getSessionUser();
        if (user != null) {
            return user.getId();
        }
        return null;
    }

}
