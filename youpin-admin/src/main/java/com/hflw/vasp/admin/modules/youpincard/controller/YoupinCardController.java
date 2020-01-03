package com.hflw.vasp.admin.modules.youpincard.controller;

import cn.hutool.db.Page;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.hflw.vasp.admin.common.enums.YoupinCardStatus;
import com.hflw.vasp.admin.modules.AbstractController;
import com.hflw.vasp.admin.modules.youpincard.dto.YoupinCardSearch;
import com.hflw.vasp.admin.modules.youpincard.model.YoupinCardListExport;
import com.hflw.vasp.admin.modules.youpincard.model.YoupinCardListModel;
import com.hflw.vasp.admin.modules.youpincard.service.YoupinCardService;
import com.hflw.vasp.utils.DateUtils;
import com.hflw.vasp.web.Pagination;
import com.hflw.vasp.web.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 优品卡订单
 */
@RestController
@RequestMapping(value = "/youpincard")
@Validated
public class YoupinCardController extends AbstractController {

    @Autowired
    private YoupinCardService youpinCardService;

    @GetMapping(value = "/search")
    public R query(YoupinCardSearch search, Page page) throws Exception {
        Pagination<YoupinCardListModel> pagination = youpinCardService.search(search, page);
        return R.ok().putPageData(pagination);
    }

    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, YoupinCardSearch search) throws Exception {
        ExcelWriter writer = null;
        OutputStream os = response.getOutputStream();

        Pagination<YoupinCardListModel> pagination = youpinCardService.search(search, null);

        List<YoupinCardListExport> exportList = new ArrayList<>();
        for (YoupinCardListModel ypModel : pagination.getList()) {
            YoupinCardListExport export = new YoupinCardListExport();
            BeanUtils.copyProperties(ypModel, export);
            export.setYpcStatus(YoupinCardStatus.getDesc(ypModel.getYpcStatus()));
            exportList.add(export);
        }
        try {
            //添加响应头信息
            response.setHeader("Content-disposition", "attachment; filename=" + "youpin_" + DateUtils.formatSerial(new Date()) + ExcelTypeEnum.XLSX.getValue());
            response.setContentType("application/msexcel;charset=UTF-8");//设置类型
            response.setHeader("Pragma", "No-cache");//设置头
            response.setHeader("Cache-Control", "no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头

            //实例化 ExcelWriter
            writer = new ExcelWriter(os, ExcelTypeEnum.XLSX, true);

            //实例化表单
            Sheet sheet = new Sheet(1, 0, YoupinCardListExport.class);
            sheet.setSheetName("优品卡订单");

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

}
