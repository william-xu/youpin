package com.hflw.vasp.controller;

import com.hflw.vasp.enums.MimeType;
import com.hflw.vasp.exception.SystemMessage;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class BaseController {

    protected Log logger = LogFactory.getLog(getClass());
    public static final String CHARSET = "UTF-8";

    /**
     * 获取客户端Ip
     *
     * @return
     */
    protected String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 流预览
     *
     * @param input
     * @param mime
     * @param response
     * @throws IOException
     */
    protected void preview(InputStream input, MimeType mime, HttpServletResponse response) throws IOException {
        if (input == null) {
            return;
        }
        if (mime == MimeType.AUTO) {
            throw SystemMessage.NOT_SUPPORT_OPERATOR.exception("预览需要设置 mime 类型,无法自动获取");
        }

        response.setContentType(mime.getContentType());
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        ServletOutputStream output = response.getOutputStream();
        IOUtils.copy(input, output);
        output.flush();
    }

    /**
     * 下载流
     *
     * @param input
     * @param mime
     * @param fileName
     * @param response
     * @throws IOException
     */
    protected void download(InputStream input, MimeType mime, String fileName, HttpServletResponse response) throws IOException {
        if (input == null) {
            return;
        }
        boolean isAuto = false;
        if (mime == MimeType.AUTO) {
            String extension = FilenameUtils.getExtension(fileName);
            mime = MimeType.parseMIME(extension);
            isAuto = true;
        }
        response.setContentType(mime.getContentType());
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");

        String suffix = mime.getSuffix();

        String encodeFileName = encodeFilename(fileName);
        if (StringUtils.isNotBlank(suffix) && !isAuto) {
            encodeFileName += ("." + mime.getSuffix());
        }

        response.setHeader("Content-Disposition", "attachment;filename=\"" + encodeFileName + "\"");
        long length = input.available();
        if (length != -1 && length != 0) {
            response.setContentLength((int) length);
        }
        ServletOutputStream output = response.getOutputStream();
        IOUtils.copy(input, output);
        output.flush();
    }

    /**
     * 编码文件名
     *
     * @param filename
     * @return
     */
    public String encodeFilename(String filename) {
        /**
         * 获取客户端浏览器和操作系统信息
         * 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)
         * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6
         */
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String agent = request.getHeader("USER-AGENT");
        try {
            if ((agent != null) && (agent.contains("MSIE"))) {
                String newFileName = URLEncoder.encode(filename, "UTF-8");
                newFileName = StringUtils.replace(newFileName, "+", "%20");
                if (newFileName.length() > 150) {
                    newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
                    newFileName = StringUtils.replace(newFileName, " ", "%20");
                }
                return newFileName;
            }
            if ((agent != null) && (agent.contains("Mozilla"))) {
                return new String(filename.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            }
            if ((agent != null) && (agent.contains("PostmanRuntime"))) {
                return new String(filename.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
            }
            return filename;
        } catch (Exception ex) {
            return filename;
        }
    }

}
