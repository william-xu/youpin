package com.hflw.vasp.eshop.modules.common.controller;

import com.hflw.vasp.annotation.AccessNoSession;
import com.hflw.vasp.eshop.modules.common.service.CommonService;
import com.hflw.vasp.utils.StringUtils;
import com.hflw.vasp.web.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 通用请求处理
 *
 * @author liuyf
 */
@Api
@RestController
@RequestMapping(value = "/common")
public class CommonController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private CommonService commonService;

    @AccessNoSession
    @ApiOperation(value = "短信验证码接口", notes = "发送短信验证码，5分钟有效")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", required = true, dataType = "String")
    })
    @GetMapping(value = "/sendVerifyCode")
    public R sendVerifyCode(String phone) {
        if (StringUtils.isEmpty(phone)) return R.error("手机号码不能为空!");

        String verifyCode = commonService.sendVerifyCode(phone);

        return R.ok().data(verifyCode);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public ModelAndView get(ModelAndView mv) {
        mv.setViewName("fileUpload");
        return mv;
    }

}
