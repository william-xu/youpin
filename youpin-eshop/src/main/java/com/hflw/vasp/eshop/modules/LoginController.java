package com.hflw.vasp.eshop.modules;


import com.hflw.vasp.eshop.common.annotation.AccessNoSession;
import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.eshop.common.exception.ResultCodeEnum;
import com.hflw.vasp.eshop.common.utils.UserUtils;
import com.hflw.vasp.eshop.modules.user.service.UserService;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.framework.components.RedisCacheUtils;
import com.hflw.vasp.modules.entity.Customer;
import com.hflw.vasp.web.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author liuyf
 * @Title UserController.java
 * @Package com.hflw.vasp.controller
 * @Description 登录
 * @date 2019年10月24日 下午2:02:54
 */
@RestController
@Validated
public class LoginController extends AbstractController {

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisCacheUtils redisCacheUtil;

    @Autowired
    private UserService userService;

    @AccessNoSession
    @PostMapping(value = "/login")
    public R login(@NotBlank(message = "手机号不能为空") String phone, @NotBlank(message = "验证码不能为空") String verifyCode, String openId) {
        logger.info("微信用户登录--》手机号phone=" + phone + ",验证码verifyCode=" + verifyCode + "小程序openId=" + openId);
        final String redisSmsKey = Constants.SMS_VERIFY_CODE_PREFIX + "." + phone;
        String realVerifyCode = (String) redisCacheUtil.getCacheObject(redisSmsKey);
        if (StringUtils.isEmpty(realVerifyCode))
            throw BusinessException.create(ResultCodeEnum.SMS_VERIFY_CODE_TIMEOUT.getCode(), ResultCodeEnum.SMS_VERIFY_CODE_TIMEOUT.getMsg());
        if (!verifyCode.equals(realVerifyCode))
            throw BusinessException.create(ResultCodeEnum.SMS_VERIFY_CODE_NOT_RIGHT.getCode(), ResultCodeEnum.SMS_VERIFY_CODE_NOT_RIGHT.getMsg());

        Customer user = userService.getUserByPhone(phone);
        if (user == null) user = new Customer();
        user.setPhone(phone);
        if (StringUtils.isNotEmpty(openId)) {
            //如果用户换手机号，miniopenid也要绑定到新手机号，并取消旧号码的关联
            Customer existUser = userService.findByWxOpenId(openId);
            if (existUser != null && !phone.equals(existUser.getPhone())) {
                existUser.setWxOpenId(null);
                userService.update(existUser);
            }
            user.setWxOpenId(openId);
        }
        Long id = userService.saveOrUpdate(user);
        user.setId(id);
        //miniOpenId保存到redis做免登录使用
        UserUtils.putSessionUser(session, user);
        redisCacheUtil.delete(redisSmsKey);
        redisCacheUtil.setCacheObject(Constants.REDIS_USER_PHONE_KEY + user.getPhone(), user, Constants.REDIS_INITDATA_VALID_TIME, TimeUnit.DAYS);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("user", user);
        resultMap.put("sessionId", session.getId());
        return R.ok().data(resultMap);
    }

    @AccessNoSession
    @RequestMapping(value = "/loginByOpenId")
    public R loginByOpenId(@NotBlank(message = "openId不能为空") String openId) {
        Customer dbUser = userService.findByWxOpenId(openId);
        //判断数据库是否存在
        if (dbUser == null)
            return R.error(ResultCodeEnum.USER_NOT_EXIST.getCode(), ResultCodeEnum.USER_NOT_EXIST.getMsg());

        Customer redisUser = (Customer) redisCacheUtil.getCacheObject(Constants.REDIS_USER_PHONE_KEY + dbUser.getPhone());
        //判断用户缓存是否存在
        if (redisUser == null)
            return R.error(ResultCodeEnum.NOT_REDIS_LOGIN.getCode(), ResultCodeEnum.NOT_REDIS_LOGIN.getMsg());
        //判断miniOpenid 是否存在并一致
        if (!openId.equals(redisUser.getMiniOpenId()))
            return R.error(ResultCodeEnum.NOT_MINIOPENID_LOGIN.getCode(), ResultCodeEnum.NOT_MINIOPENID_LOGIN.getMsg());

        UserUtils.putSessionUser(session, dbUser);
        redisCacheUtil.setCacheObject(Constants.REDIS_USER_PHONE_KEY + dbUser.getPhone(), dbUser, Constants.REDIS_INITDATA_VALID_TIME, TimeUnit.DAYS);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("dbUser", dbUser);
        resultMap.put("sessionId", session.getId());
        return R.ok().data(resultMap);
    }

    @RequestMapping(value = "/index")
    public R index() {
        //登录成功，进入首页
        return R.ok();
    }

    @RequestMapping(value = "/logout")
    public R logout() {
        Customer user = getSessionUser();
        redisCacheUtil.delete(Constants.REDIS_USER_PHONE_KEY + user.getPhone());
        UserUtils.clearSession(session);
        return R.ok();
    }

}
