package com.hflw.vasp.eshop.modules.user.controller;


import com.hflw.vasp.annotation.SysLog;
import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.user.model.UserAddressModel;
import com.hflw.vasp.eshop.modules.user.service.UserService;
import com.hflw.vasp.modules.entity.Customer;
import com.hflw.vasp.modules.entity.CustomerAddress;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        Customer customer = getSessionUser();
        return R.ok().data(customer);
    }

    @RequestMapping(value = "/address/list")
    public R addressList() {
        List<CustomerAddress> list = userService.findAllAddressByUserId(getUserId());
        return R.ok().data(list);
    }

    @SysLog
    @RequestMapping(value = "/address/add")
    public R addressAdd(UserAddressModel model) {
        userService.addUserAddress(getUserId(), model);
        return R.ok();
    }

    @SysLog
    @RequestMapping(value = "/address/update")
    public R addressUpdate(UserAddressModel model) {
        userService.updateUserAddress(model);
        return R.ok();
    }

    @RequestMapping(value = "/address/delete")
    public R addressDel(Long id) {
        userService.deleteUserAddressById(id);
        return R.ok();
    }

}
