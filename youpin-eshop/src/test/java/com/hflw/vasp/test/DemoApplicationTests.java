package com.hflw.vasp.test;

import com.hflw.vasp.framework.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private RedisService redisService;

    @Test
    public void contextLoads() {
        long startMillis = System.currentTimeMillis();
        String orderIdPrefix = redisService.getOrderIdPrefix(new Date());
        for (int i = 0; i < 10; i++) {
            Long aLong = redisService.orderId(orderIdPrefix);
            System.out.println(aLong);
        }
        long endMillis = System.currentTimeMillis();
        System.out.println("生成速度:" + (endMillis - startMillis) + ",单位毫秒");
    }

}