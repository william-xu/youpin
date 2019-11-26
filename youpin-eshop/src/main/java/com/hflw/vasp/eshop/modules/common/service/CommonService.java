package com.hflw.vasp.eshop.modules.common.service;

import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.framework.components.RedisCacheUtils;
import com.hflw.vasp.system.dao.ICommonDao;
import com.hflw.vasp.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author qurong
 * @Title CommonServiceImpl.java
 * @Package com.qurong.service.impl
 * @Description TODO
 * @date 2018年10月22日 下午3:01:25
 */
@Service
public class CommonService {

    private Logger logger = LoggerFactory.getLogger(CommonService.class);

    @Autowired
    private ICommonDao commonDao;

    @Autowired
    private SmsService smsService;

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisCacheUtils redisCacheUtil;

    @SuppressWarnings("unchecked")
    public String sendVerifyCode(String phone) {
        String smsKey = Constants.SMS_VERIFY_CODE_PREFIX + "." + phone;
        String smsCode = StringUtils.generateRandomCode(true, 4);
        redisCacheUtil.setCacheObject(smsKey, smsCode, Constants.SMS_VERIFY_CODE_TIMEOUT, TimeUnit.SECONDS);
        logger.info("发送手机号phone=" + phone + ">>>smsCode=" + smsCode);
        smsService.sendVerifyCode(phone, smsCode);
        return smsCode;
    }


    public synchronized String getContractSerialNum(String sequenceName) {
        String nextVal = null;//commonMapper.getNextVal(sequenceName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(new Date());
        return Constants.ORDER_NO_PREFIX + dateNowStr + StringUtils.zeroFill(nextVal, 6);
    }
}