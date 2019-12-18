//package com.hflw.vasp.admin;
//
//import com.alibaba.fastjson.JSON;
//import com.hflw.vasp.AdminApplication;
//import com.hflw.vasp.admin.modules.order.dto.OrderSearch;
//import com.hflw.vasp.admin.modules.order.model.OrderListModel;
//import com.hflw.vasp.admin.modules.order.service.OrderService;
//import com.hflw.vasp.utils.ReflectUtils;
//import com.hflw.vasp.web.Pagination;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = AdminApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class DemoApplicationTests {
//
//    @Autowired
//    private OrderService orderService;
//
//    @Test
//    public void findOrderCriteria4() throws Exception {
//        OrderSearch query = new OrderSearch();
//        query.setPageNumber(1);
//        query.setPageSize(10);
//        Pagination list = orderService.search(query);
//        System.out.println(list.getTotal());
//        System.out.println(JSON.toJSONString(list));
//
//        List<OrderListModel> modelList = ReflectUtils.castEntity(list.getList(), OrderListModel.class);
//        System.out.println(JSON.toJSONString(modelList));
//    }
//
//}