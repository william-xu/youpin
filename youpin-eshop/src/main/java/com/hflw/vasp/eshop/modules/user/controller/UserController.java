package com.hflw.vasp.eshop.modules.user.controller;


import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.user.service.UserService;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuyf
 * @Title UserController.java
 * @Package com.hflw.vasp.controller
 * @Description 登录
 * @date 2019年10月24日 下午2:02:54
 */
@RestController
@RequestMapping(value = "/user")
@Validated
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/info")
    public R index() {

        return R.ok();
    }

    @RequestMapping(value = "/addresslist")
    public R addressList() {

        return R.ok();
    }

    @RequestMapping(value = "/address/add")
    public R addressAdd() {

        return R.ok();
    }

    @RequestMapping(value = "/address/update")
    public R addressUpdate() {

        return R.ok();
    }

    @RequestMapping(value = "/address/delete")
    public R addressDel() {

        return R.ok();
    }

}
