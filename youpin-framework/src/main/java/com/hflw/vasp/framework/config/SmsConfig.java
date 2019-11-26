package com.hflw.vasp.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsConfig {
    String accessKeyId;
    String accessKeySecret;
    String signName;
    String verifyCodeTemplate;
}
