package com.hflw.vasp.admin.common.config;

import com.hflw.vasp.framework.config.CommonBeansConfig;
import com.hflw.vasp.framework.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        PropertiesConfigs.class,
        CommonBeansConfig.class,
        GlobalExceptionHandler.class
})
public class ImportBeansConfigs {

}
