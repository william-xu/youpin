package com.hflw.vasp.eshop.modules.common.service;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonResponse;
import com.hflw.vasp.framework.components.SmsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SmsService {

    private final Logger LOGGER = LoggerFactory.getLogger(SmsService.class);

    @Autowired
    private SmsUtils smsUtils;

    public CommonResponse sendVerifyCode(String phone, String code) {
        CommonResponse response = smsUtils.sendVerifyCode(phone, code);
        if (response != null && response.getHttpStatus() == 200) {
            String data = response.getData();
            Map map = JSON.parseObject(data, Map.class);
            LOGGER.info("短信发送状态:{}" + phone + "=====" + code + "=====" + map.get("Message"));
        }

        //以下为Api的测试代码，不做理会即可
        boolean success = response.getHttpResponse().isSuccess();  //true
        int status = response.getHttpResponse().getStatus(); //200
        int httpStatus = response.getHttpStatus(); //200
        return response;
    }

}
