package com.hflw.vasp.eshop.modules.common.service;

import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.framework.components.RedisCacheUtils;
import com.hflw.vasp.framework.service.RedisService;
import com.hflw.vasp.system.dao.ICommonDao;
import com.hflw.vasp.utils.SnowFlake;
import com.hflw.vasp.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author liuyf
 * @Title CommonServiceImpl.java
 * @Package com.hflw.vasp.service.impl
 * @Description TODO
 * @date 2019年10月22日 下午3:01:25
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

    @Autowired
    private RedisService redisService;

    @SuppressWarnings("unchecked")
    public String sendVerifyCode(String phone) {
        String smsCode = StringUtils.generateRandomCode(true, 4);

        logger.info("准备发送手机号>>>" + phone + "短信验证码>>>" + smsCode);
        smsService.sendVerifyCode(phone, smsCode);

        String smsKey = Constants.SMS_VERIFY_CODE_PREFIX + "." + phone;
        redisCacheUtil.setCacheObject(smsKey, smsCode, Constants.SMS_VERIFY_CODE_TIMEOUT, TimeUnit.SECONDS);
        return smsCode;
    }

    public synchronized String getContractSerialNum(String sequenceName) {
        String nextVal = null;//commonMapper.getNextVal(sequenceName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(new Date());
        return Constants.ORDER_NO_PREFIX + dateNowStr + StringUtils.zeroFill(nextVal, 6);
    }

    public long getGlobalUniqueId() {
        // 锁表(用于判断现在是否可以创建订单)一小时的超时时间
//        if (!redisService.addRedisLock(ConstantKeys.REDIS_LOCK_MAX_ORDER_NUMBER, 2000, 2)) {
//            return 0;
//        }
        long id = SnowFlake.nextId();
//        // 解锁
//        redisService.delRedisLock(ConstantKeys.REDIS_LOCK_MAX_ORDER_NUMBER);
        return id;
    }

}
