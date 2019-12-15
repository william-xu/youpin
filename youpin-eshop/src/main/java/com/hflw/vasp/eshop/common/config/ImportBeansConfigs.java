package com.hflw.vasp.eshop.common.config;

import com.hflw.vasp.framework.components.SMSProperties;
import com.hflw.vasp.framework.components.UploadProperties;
import com.hflw.vasp.framework.config.CommonBeansConfig;
import com.hflw.vasp.framework.config.RedisConfig;
import com.hflw.vasp.framework.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        CommonBeansConfig.class,
        SMSProperties.class,
        RedisConfig.class,
        UploadProperties.class,
        GlobalExceptionHandler.class
})
public class ImportBeansConfigs {

}
