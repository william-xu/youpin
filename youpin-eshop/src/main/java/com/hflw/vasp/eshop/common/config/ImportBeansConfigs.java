package com.hflw.vasp.eshop.common.config;

import com.hflw.vasp.framework.config.CommonBeansConfig;
import com.hflw.vasp.framework.config.SmsConfig;
import com.hflw.vasp.framework.config.Swagger2Config;
import com.hflw.vasp.framework.config.UploadProperties;
import com.hflw.vasp.framework.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        CommonBeansConfig.class,
        SmsConfig.class,
        Swagger2Config.class,
        UploadProperties.class,
        GlobalExceptionHandler.class
})
public class ImportBeansConfigs {

}
