package com.hflw.vasp.task.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OrderJob {

    @Scheduled(cron = "0/5 * * * * ?")
    public void execute() {
        log.info("Order Quartz Job执行时间: " + new Date());

        log.info("Order Quartz Job执行完成: " + new Date());
    }

}
