package com.hflw.vasp.admin.modules.order.controller;


import cn.hutool.db.Page;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.hflw.vasp.admin.common.enums.OrderStatus;
import com.hflw.vasp.admin.modules.AbstractController;
import com.hflw.vasp.admin.modules.order.dto.OrderLogisticsDTO;
import com.hflw.vasp.admin.modules.order.dto.OrderSearch;
import com.hflw.vasp.admin.modules.order.model.OrderDetails;
import com.hflw.vasp.admin.modules.order.model.OrderListExport;
import com.hflw.vasp.admin.modules.order.model.OrderListModel;
import com.hflw.vasp.admin.modules.order.service.OrderService;
import com.hflw.vasp.modules.entity.OrderLogistics;
import com.hflw.vasp.utils.DateUtils;
import com.hflw.vasp.web.Pagination;
import com.hflw.vasp.web.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author liuyf
 * @Title OrderController.java
 * @Package com.hflw.vasp.controller
 * @Description 订单，商品订单
 * @date 2019年10月24日 下午2:02:54
 */
@RestController
@RequestMapping(value = "/order")
@Validated
public class OrderController extends AbstractController {

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/search")
    public R query(OrderSearch search, Page page) throws Exception {
        Pagination<OrderListModel> pagination = orderService.search(search, page);
        return R.ok().putPageData(pagination);
    }

    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, OrderSearch search) throws Exception {
        ExcelWriter writer = null;
        OutputStream os = response.getOutputStream();

        Pagination<OrderListModel> pagination = orderService.search(search, null);

        List<OrderListExport> exportList = new ArrayList<>();
        for (OrderListModel olModel : pagination.getList()) {
            OrderListExport export = new OrderListExport();
            BeanUtils.copyProperties(olModel, export);
            export.setStatus(OrderStatus.getDesc(olModel.getStatus()));
            exportList.add(export);
        }
        try {
            //添加响应头信息
            response.setHeader("Content-disposition", "attachment; filename=" + "order_" + DateUtils.formatSerial(new Date()) + ExcelTypeEnum.XLSX.getValue());
            response.setContentType("application/msexcel;charset=UTF-8");//设置类型
            response.setHeader("Pragma", "No-cache");//设置头
            response.setHeader("Cache-Control", "no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头

            //实例化 ExcelWriter
            writer = new ExcelWriter(os, ExcelTypeEnum.XLSX, true);

            //实例化表单
            Sheet sheet = new Sheet(1, 0, OrderListExport.class);
            sheet.setSheetName("商品订单");

            //输出
            writer.write(exportList, sheet);
            writer.finish();
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/info")
    public R query(Long id) {
        OrderDetails od = orderService.getDetailsById(id);
        return R.ok().data(od);
    }

    /**
     * 绑定物流信息
     *
     * @param logisticsDTO
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/bindlogistics")
    public R bindLogistics(@Valid OrderLogisticsDTO logisticsDTO, BindingResult bindingResult) {
        checkDTOParams(bindingResult);

        OrderLogistics logistics = logisticsDTO.convertTo();
        orderService.bindLogistics(logistics);

        return R.ok();
    }

    /**
     * 解绑物流信息
     *
     * @param logisticsDTO
     * @param bindingResult
     * @return
     */
    @PostMapping(value = "/untyinglogistics")
    public R untyingLogistics(@Valid OrderLogisticsDTO logisticsDTO, BindingResult bindingResult) {
        return R.ok();
    }

    /**
     * 修改商品订单
     *
     * @param orderId
     * @param status
     * @return
     */
    @PostMapping(value = "/change")
    public R change(@NotNull Long orderId, @NotNull Integer status) {
        orderService.change(orderId, status);
        return R.ok();
    }

    /**
     * 修改优品订单
     *
     * @param orderId
     * @param status
     * @return
     */
    @PostMapping(value = "/changeYoupin")
    public R changeYoupin(@NotNull Long orderId, @NotNull Integer status) {
        orderService.change(orderId, status);
        return R.ok();
    }

}
