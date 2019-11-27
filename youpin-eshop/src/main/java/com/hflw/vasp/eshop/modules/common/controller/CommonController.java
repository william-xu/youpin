package com.hflw.vasp.eshop.modules.common.controller;

import com.hflw.vasp.eshop.common.annotation.AuthCheck;
import com.hflw.vasp.eshop.common.exception.ResultCodeEnum;
import com.hflw.vasp.eshop.modules.common.service.CommonService;
import com.hflw.vasp.utils.StringUtils;
import com.hflw.vasp.web.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 通用请求处理
 *
 * @author qurong
 */
@RestController
@RequestMapping(value = "/common")
public class CommonController {
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private CommonService commonService;

    @AuthCheck
    @RequestMapping(value = "/sendVerifyCode")
    public R sendVerifyCode(String phone) {
        if (StringUtils.isEmpty(phone)) return R.error("手机号码不能为空!");

        String verifyCode = commonService.sendVerifyCode(phone);
        logger.info("手机号：" + phone + "的短信验证码：" + verifyCode);
        return R.ok();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public ModelAndView get(ModelAndView mv) {
        mv.setViewName("fileUpload");
        return mv;
    }

}
