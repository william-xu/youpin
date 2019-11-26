package com.hflw.vasp.utils;

import javax.servlet.ServletException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 生成验证码的工具类，不依赖具体环境
 */
public class CaptchaUtil {
    /**
     * 生成验证码<br>
     * <b>注意：验证码图片宽度必须大于验证码文字的左边距，验证码图片高度必须大于验证码文字的上边距<b>
     * @param captchaWidth 验证码图片宽度
     * @param captchaHeight 验证码图片高度
     * @param textmarginLeft 验证码文字的左边距
     * @param textmarginTop 验证码文字的上边距
     * @param textSpacing 验证码文字的间距
     * @param groundColor 验证码图片背景颜色
     * @param borderColor 验证码边框颜色
     * @param textfont 验证码文字大小和样式
     * @param captchaText 验证文字
     * @param addNoise 是否加入干扰线
     * @param needBorder 是否加入边框
     * @return BufferedImage
     * @throws ServletException
     */
    public static BufferedImage generateCaptcha(int captchaWidth, int captchaHeight, int textmarginLeft,
            int textmarginTop, int textSpacing, Color groundColor, Color borderColor, Font textfont, String captchaText,
            boolean addNoise, boolean needBorder) throws ServletException {
        if (captchaWidth < textmarginLeft) {
            throw new ServletException("captchaWidth must be greater than textmarginLeft");
        }
        if (captchaHeight < textmarginTop) {
            throw new ServletException("captchaHeight must be greater than textmarginTop");
        }
        // 生成随机类
        Random random = new Random();
        
        // 在内存中创建图象

        BufferedImage image = new BufferedImage(captchaWidth, captchaHeight, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        
        // 设定背景色
        g.setColor(groundColor);
        g.fillRect(0, 0, captchaWidth, captchaHeight);
        // 设定字体
        g.setFont(textfont);

        if (needBorder) {
            // 画边框
            g.setColor(borderColor);
            g.drawRect(0, 0, captchaWidth - 1, captchaHeight - 1);
        }
        // 随机产生155条干扰线
        if (addNoise) {
            g.setColor(getRandColor(160, 200));
            for (int i = 0; i < 155; i++) {
                int x = random.nextInt(captchaWidth);
                int y = random.nextInt(captchaHeight);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x, y, x + xl, y + yl);
            }
        }
        for (int i = 0; i < captchaText.length(); i++) {
            String rand = String.valueOf(captchaText.charAt(i));
            // 将验证码显示到图象中
            g.setColor(new Color(random.nextInt(220), random.nextInt(220), random.nextInt(220)));
            g.drawString(rand, textmarginLeft * i + textSpacing, textmarginTop);
        }

        
        g.dispose();

        return image;
    }

    /**
     * 生成验证码文字
     * @param randomSeed 随机种子字符串
     * @param size 验证码文字的长度
     * @return 验证码文字
     */
    public static String getSessionRad(String randomSeed, int size) {
        StringBuffer sessionRad = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            String rand = String.valueOf(randomSeed.charAt(random.nextInt(randomSeed.length())));
            sessionRad.append(rand);
        }
        return sessionRad.toString();
    }

    /**
     * 给定范围获得随机颜色
     * @param fc
     * @param bc
     * @return
     */
    public static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > bc) {
            int temp = fc;
            fc = bc;
            bc = temp;
        }
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
