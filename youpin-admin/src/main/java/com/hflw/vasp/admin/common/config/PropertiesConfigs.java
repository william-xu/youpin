package com.hflw.vasp.admin.common.config;

import com.hflw.vasp.admin.common.config.shiro.ShiroProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ShiroProperties.class})
public class PropertiesConfigs {
}
