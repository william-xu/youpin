package com.hflw.vasp.controller;


import com.hflw.vasp.enums.MimeType;
import com.hflw.vasp.exception.SystemMessage;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class BaseReportController extends BaseController {

    @GetMapping(value = "/downTemplates")
    @ApiOperation(value = "下载导入模版")
    public void downTemplates(HttpServletResponse response, String fileName) throws IOException {
        if (StringUtils.isBlank(fileName)) {
            throw SystemMessage.ARGS_NULL.exception("模板名称必传");
        }

        InputStream resourceAsStream = BaseReportController.class.getResourceAsStream("/excelTemplate/" + fileName);
        MimeType mimeType = MimeType.EXCEL2007;
        if (fileName.endsWith(".xls")) {
            mimeType = MimeType.EXCEL2003;
        }
        String baseName = FilenameUtils.getBaseName(fileName);
        download(resourceAsStream, mimeType, baseName, response);
    }

    @GetMapping(value = "/downFailRecords")
    @ApiOperation(value = "下载导出失败记录")
    public void downFailRecords(HttpServletResponse response, String address, String modulName) throws IOException {
        CloseableHttpClient httpClient = null;//HttpUtil.getHttpClient();
        HttpGet httpGet = new HttpGet(address);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();

        if (StringUtils.isBlank(modulName)) {
            int lastIndexOf = address.lastIndexOf('/');
            //如果未传文件名,获取随机名
            modulName = address.substring(lastIndexOf + 1);
            modulName = FilenameUtils.getBaseName(modulName);
        }
        download(httpEntity.getContent(), MimeType.EXCEL2007, modulName, response);
        HttpClientUtils.closeQuietly(httpResponse);
    }

    /**
     * 导出通用泛型
     *
     * @param response
     * @param list
     * @param fileName
     * @throws IOException
     */
//    protected void exportObject(HttpServletResponse response, List list, String fileName) throws IOException {
//        InputStream inputStream = null;
//        if (CollectionUtils.isEmpty(list)) {
//            Workbook workbook = com.glsx.excel.poi.ExcelUtil.createEmptyWorkbook(Version.EXCEL2007, "无数据");
//            inputStream = ExcelUtil.toInputStream(workbook);
//            download(inputStream, MimeType.EXCEL2007, fileName, response);
//            return;
//        }
//        ExcelUtil<Object> exportModelExcelUtil = new ExcelUtil<Object>(1000);
//        SXSSFWorkbook sxssfWorkbook = null;
//        try {
//            sxssfWorkbook = (SXSSFWorkbook) exportModelExcelUtil.exportDefaultStyle(fileName, list);
//            inputStream = exportModelExcelUtil.toInputStream();
//            download(inputStream, MimeType.EXCEL2007, fileName, response);
//        } finally {
//            if(sxssfWorkbook != null) {
//                sxssfWorkbook.dispose();
//            }
//            IOUtils.closeQuietly(inputStream);
//        }
//    }
}
