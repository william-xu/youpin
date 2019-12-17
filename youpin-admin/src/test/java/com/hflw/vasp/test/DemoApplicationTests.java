//package com.hflw.vasp.test;
//
//import com.hflw.vasp.admin.modules.order.dto.OrderQuery;
//import com.hflw.vasp.admin.modules.order.service.OrderService;
//import com.hflw.vasp.framework.service.RedisService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.management.Query;
//import java.util.Date;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class DemoApplicationTests {
//
//    @Autowired
//    private OrderService orderService;
//
//    @Test
//    public void contextLoads() {
//        OrderQuery query = new OrderQuery();
//        query.setPageNumber(1);
//        query.setPageSize(10);
//        orderService.findOrderCriteria2(query);
//    }
//
//}