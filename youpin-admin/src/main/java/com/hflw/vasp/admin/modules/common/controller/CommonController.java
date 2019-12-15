package com.hflw.vasp.admin.modules.common.controller;

import com.google.code.kaptcha.Producer;
import com.hflw.vasp.admin.modules.AbstractController;
import com.hflw.vasp.admin.modules.common.service.CommonService;
import com.hflw.vasp.annotation.AccessNoSession;
import com.hflw.vasp.framework.components.PropertiesUtils;
import com.hflw.vasp.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 通用请求处理
 *
 * @author liuyf
 */
@Api
@RestController
@RequestMapping(value = "/common")
public class CommonController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private Producer producer;

    @Autowired
    private CommonService commonService;

    @AccessNoSession
    @ApiOperation(value = "验证码接口")
    @GetMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成文字验证码
        String text = PropertiesUtils.getProperty("captcha.sessionRad");
        if (StringUtils.isNullOrEmpty(text)) text = producer.createText();

        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        //保存到 session
        request.getSession().setAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY, text);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public ModelAndView get(ModelAndView mv) {
        mv.setViewName("fileUpload");
        return mv;
    }

}
