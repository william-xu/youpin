package com.hflw.vasp.admin.common.config.shiro;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.shiro")
public class ShiroProperties {

    private Long timeout;

}
