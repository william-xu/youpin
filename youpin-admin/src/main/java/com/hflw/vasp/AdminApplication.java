package com.hflw.vasp;

import com.hflw.vasp.admin.common.config.ImportBeansConfigs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Import({ImportBeansConfigs.class})
@ComponentScan(basePackages = {
        "com.hflw.vasp.admin.*",
        "com.hflw.vasp.framework.components",
        "com.hflw.vasp.framework.service",
        "com.hflw.vasp.entity"})
@EnableJpaRepositories(basePackages = "com.hflw.vasp.*.dao")
@EnableTransactionManagement
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
