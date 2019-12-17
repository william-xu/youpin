/**
 * Copyright (C), 2015-2018, 广联赛讯有限公司
 * FileName: WechatUtils
 * Author:   liuyf
 * Date:     2018/4/27 12:56
 * Description: 微信工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hflw.vasp.eshop.common.utils.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.framework.components.PropertiesUtils;
import com.hflw.vasp.framework.components.RedisCacheUtils;
import com.hflw.vasp.framework.constant.ConstantKeys;
import com.hflw.vasp.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈微信工具类〉
 *
 * @author liuyf
 * @create 2018/4/27
 * @since 1.0.0
 */
@Slf4j
@Component("wechatUtils")
public class WechatUtils {

    public String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=$APPID$&secret=$SECRET$";
    public String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=$ACCESS_TOKEN$&type=jsapi";
    public String GET_MEDIA_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=$ACCESS_TOKEN$&media_id=$MEDIA_ID$";
    public String GET_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=$APPID$&secret=$SECRET$&code=$CODE$&grant_type=authorization_code";
    public String GET_MINI_OPENID_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=$APPID$&secret=$SECRET$&js_code=$JSCODE$&grant_type=authorization_code";
    public String SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    public String GET_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=$ACCESS_TOKEN$&openid=$OPENID$&lang=zh_CN";

    @Resource
    private RedisCacheUtils redisCacheUtil;

    public Map<String, String> sign(String appid, String secret, String url) throws Exception {
        Map<String, String> ret = new HashMap<>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String jsapi_ticket = getWechatTicket(appid, secret, getWechatToken(appid, secret));
        String str;
        String signature = "";
        //注意这里参数名必须全部小写，且必须有序
        str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes(StandardCharsets.UTF_8));
            signature = byteToHexString(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        ret.put("appId", appid);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("url", url);
        return ret;
    }

    private static String byteToHexString(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 获取微信token和ticket
     *
     * @return
     * @throws Exception
     */
    public Map<String, String> getWechatTokenAndTicket(String appid, String secret) throws Exception {
        Map<String, String> wtt = new HashMap<String, String>();

        String access_token = getWechatToken(appid, secret);
        String ticket = getWechatTicket(appid, secret, access_token);

        wtt.put("token", access_token);
        wtt.put("ticket", ticket);
        wtt.put("appid", appid);
        return wtt;
    }

    /**
     * 获取微信token和opendId
     *
     * @param appid
     * @param secret
     * @param code
     * @return
     * @throws Exception
     */
    public Map<String, Object> getWechatTokenAndOpenId(String appid, String secret, String code, Integer type)
            throws Exception {
        Map<String, Object> wto = new HashMap<>();
        String url = Constants.MINI == type ? GET_MINI_OPENID_URL : GET_OPENID_URL;
        url = Constants.MINI == type
                ? url.replace("$APPID$", appid).replace("$SECRET$", secret).replace("$JSCODE$", code).replace("\r", "").replace("\n", "")
                : url.replace("$APPID$", appid).replace("$SECRET$", secret).replace("$CODE$", code).replace("\r", "").replace("\n", "");
        HttpUtils httpUtils = new HttpUtils();
        String result = httpUtils.get(url, null);
        log.info(url + " 获取openId接口返回：" + result);
        if (StringUtils.isNotEmpty(result)) {
            JSONObject openIdJO = JSON.parseObject(result);
            for (String key : openIdJO.keySet()) {
                wto.put(key, openIdJO.getString(key));
            }
        }
        wto.put("appid", appid);
        return wto;
    }

    /**
     * 获取微信token
     *
     * @param appid
     * @param secret
     * @return
     * @throws Exception
     */
    public String getWechatToken(String appid, String secret) throws Exception {
        String tokenCacheKey = Constants.WECHAT_TOKEN_KEY + appid;
        String access_token = (String) redisCacheUtil.getCacheObject(tokenCacheKey);
        //log.info("公众号缓存token：" + access_token);
        if (StringUtils.isEmpty(access_token)) {
            access_token = getAndCacheWechatToken(appid, secret);
        }
        return access_token;
    }

    /**
     * 获取并缓存微信token
     *
     * @param appid
     * @param secret
     * @return
     * @throws Exception
     */
    private String getAndCacheWechatToken(String appid, String secret) throws Exception {
        String access_token = "";
        String url = GET_TOKEN_URL.replace("$APPID$", appid).replace("$SECRET$", secret);
        HttpUtils httpUtils = new HttpUtils();
        String result = httpUtils.get(url, null);
        log.info(url + " 获取token接口返回：" + result);
        //{"access_token":"10_hMYT_8NG0GFfM4jkqVSNdWT4QaVIb3SRVhdOfsyj3AkPxXHmeY9uXwC43WgpwJ3xWJt2Ti7RuiOWEVDPVD1sI3ksPaZxc8x2UBsRfElJNM0","expires_in":7200,"refresh_token":"10_In3tQS4WISR5vDdxasxh4j2017NW_VfKr6S2EUITlqk0Iy83rUQEUufbZxoA2NrHZqSX-O8_8O5eEhELSx54X860ttGLh0I0BGtAlVoVeMs","openid":"oxAQwwMiYESDE8--YOopaAl6F4uo","scope":"snsapi_userinfo"}
        if (StringUtils.isNotEmpty(result)) {
            JSONObject tokenJO = JSON.parseObject(result);
            if (tokenJO.containsKey("access_token")) {
                access_token = tokenJO.getString("access_token");
                long expires_in = tokenJO.getLongValue("expires_in");

                String tokenCacheKey = Constants.WECHAT_TOKEN_KEY + appid;
                redisCacheUtil.setCacheObject(tokenCacheKey, access_token, expires_in, TimeUnit.SECONDS);
            }
        }
        return access_token;
    }

    /**
     * 获取微信ticket
     *
     * @param appid
     * @param secret
     * @param access_token
     * @return
     * @throws Exception
     */
    public String getWechatTicket(String appid, String secret, String access_token) throws Exception {
        String ticketCacheKey = Constants.WECHAT_TICKET_KEY + appid;
        String ticket = (String) redisCacheUtil.getCacheObject(ticketCacheKey);
//        log.info("公众号缓存ticket：" + ticket);
        if (StringUtils.isEmpty(ticket)) {
            String url = GET_TICKET_URL.replace("$ACCESS_TOKEN$", access_token);
            HttpUtils httpUtils = new HttpUtils();
            String result = httpUtils.get(url, null);
            //{"errcode":0,"errmsg":"ok","ticket":"kgt8ON7yVITDhtdwci0qecVGaUkK4of6P4yBMM24dNWF-d8bPguhmuKFE88bG1x85CnMARKyTniiHR6NtL55bw","expires_in":7200}
            //{"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest hint: [KWDItA0150a456!]"}
            log.info(url + " 获取ticket返回：" + result);
            JSONObject ticketJO = JSON.parseObject(result);
            if (ticketJO.containsKey("ticket")) {
                ticket = ticketJO.getString("ticket");
                long expires_in = ticketJO.getLongValue("expires_in");

                redisCacheUtil.setCacheObject(ticketCacheKey, ticket, expires_in, TimeUnit.SECONDS);
            } else {
                if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(secret)) return ticket;

                access_token = getWechatToken(appid, secret);
                ticket = getWechatTicket(null, null, access_token);
            }
        }
        return ticket;
    }

    /**
     * 获取微信ticket
     *
     * @param appid
     * @param secret
     * @param openId
     * @return
     * @throws Exception
     */
    public Map<String, Object> getWechatUserInfo(String appid, String secret, String openId) throws Exception {
        Map<String, Object> wu = new HashMap<>();
        //获取token
        String accessToken = getWechatToken(appid, secret);
        if (StringUtils.isNotEmpty(accessToken)) {
            String userCacheKey = Constants.WECHAT_USERINFO_KEY + openId;
            JSONObject userJO = (JSONObject) redisCacheUtil.getCacheObject(userCacheKey);
            if (userJO == null) {
                String url = GET_USER_INFO_URL.replace("$ACCESS_TOKEN$", accessToken).replace("$OPENID$", openId);
                HttpUtils httpUtils = new HttpUtils();
                String result = httpUtils.get(url, null);
                log.info(url + " 获取UserInfo返回：" + result);
                userJO = JSON.parseObject(result);
//            成功
//            {
//                "subscribe": 1,
//                    "openid": "o6_bmjrPTlm6_2sgVt7hMZOPfL2M",
//                    "nickname": "Band",
//                    "sex": 1,
//                    "language": "zh_CN",
//                    "cities": "Guangzhou",
//                    "province": "Guangdong",
//                    "country": "China",
//                    "headimgurl":  "http://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
//                    "subscribe_time": 1382694957,
//                    "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL",
//                    "remark": "",
//                    "groupid": 0,
//                    "tagid_list":[128,2],
//                    "subscribe_scene": "ADD_SCENE_QR_CODE",
//                    "qr_scene": 98765,
//                    "qr_scene_str": ""
//            }
//            失败
//            {"errcode":40001,"errmsg":"invalid credential, access_token is invalid or not latest hint: [fBBCDa06263401!]"}
                if (userJO.containsKey("errcode") && 40001 == userJO.getIntValue("errcode")) {
                    accessToken = getAndCacheWechatToken(appid, secret);//重新获取token
                    url = GET_USER_INFO_URL.replace("$ACCESS_TOKEN$", accessToken).replace("$OPENID$", openId);
                    result = httpUtils.get(url, null);
                    log.info(url + " 重新获取UserInfo返回：" + result);
                    userJO = JSON.parseObject(result);
                }
                redisCacheUtil.setCacheObject(userCacheKey, userJO, ConstantKeys.DAYS_30, TimeUnit.DAYS);
            }
            wu.putAll(userJO);
        }
        return wu;
    }

    /**
     * 获取微信token和opendId
     *
     * @param appid
     * @param secret
     * @param code
     * @return
     * @throws Exception
     */
//    public Map<String, Object> getWechatTokenAndOpenId(String appid, String secret, String code) throws Exception {
//        Map<String, Object> wto = new HashMap<>();
//
//        String url = GET_OPENID_URL.replace("$APPID$", appid).replace("$SECRET$", secret).replace("$JSCODE$", code);
//        HttpUtils httpUtils = new HttpUtils();
//        String result = httpUtils.get(url, null);
//        log.info(url + " 获取openId接口返回：" + result);
////            {
////                "access_token": "OezXcEiiBSKSxW0eoylIeAsR0GmYd1awCffdHgb4fhS_KKf2CotGj2cBNUKQQvj-G0ZWEE5-uBjBz941EOPqDQy5sS_GCs2z40dnvU99Y5AI1bw2uqN--2jXoBLIM5d6L9RImvm8Vg8cBAiLpWA8Vw",
////                "expires_in": 7200,
////                "refresh_token": "OezXcEiiBSKSxW0eoylIeAsR0GmYd1awCffdHgb4fhS_KKf2CotGj2cBNUKQQvj-G0ZWEE5-uBjBz941EOPqDQy5sS_GCs2z40dnvU99Y5CZPAwZksiuz_6x_TfkLoXLU7kdKM2232WDXB3Msuzq1A",
////                "openid": "oLVPpjqs9BhvzwPj5A-vTYAX3GLc",
////                "scope": "snsapi_userinfo,"
////            }
//        if (StringUtils.isNotEmpty(result)) {
//            JSONObject openIdJO = JSON.parseObject(result);
//            for (String key : openIdJO.keySet()) {
//                wto.put(key, openIdJO.getString(key));
//            }
//        }
//        wto.put("appid", appid);
//        return wto;
//    }

    /**
     * 获取媒体文件
     *
     * @param mediaId  媒体文件id
     * @param savePath 文件在本地服务器上的存储路径
     */
    public String downloadMedia(String mediaId, String savePath) {
        String appid = PropertiesUtils.getProperty(Constants.WECHAT_APPID);
        String secret = PropertiesUtils.getProperty(Constants.WECHAT_SECRET);
        return downloadMedia(mediaId, savePath, appid, secret);
    }

    /**
     * 获取媒体文件
     *
     * @param mediaId  媒体文件id
     * @param savePath 文件在本地服务器上的存储路径
     * @param appid
     * @param secret
     */
    public String downloadMedia(String mediaId, String savePath, String appid, String secret) {
        HttpURLConnection conn = null;
        FileOutputStream fos = null;
        InputStream is = null;
        String fileName = "";
        try {
            String accessToken = getWechatToken(appid, secret);
            if (StringUtils.isNotEmpty(accessToken)) {
                // 拼接请求地址
                String requestUrl = GET_MEDIA_URL.replace("$ACCESS_TOKEN$", accessToken).replace("$MEDIA_ID$", mediaId);
                URL url = new URL(requestUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");
                System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
                System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
                log.info(System.getProperty("sun.net.client.defaultConnectTimeout"));
                log.info(System.getProperty("sun.net.client.defaultReadTimeout"));
                conn.connect();

                String contentType = conn.getHeaderField("Content-Type");
                if (!savePath.endsWith("/")) savePath += File.separator;

                // 根据内容类型获取扩展名
                String fileExt = getFileexpandedName(contentType);
                // 将mediaId作为文件名
                fileName = mediaId + fileExt;

                is = conn.getInputStream();
                fos = new FileOutputStream(new File(savePath + fileName));
                byte[] data = new byte[10240];
                int len = 0;
                while ((len = is.read(data)) != -1) {
                    fos.write(data, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);
            if (conn != null) conn.disconnect();
        }
        return fileName;
    }

    /**
     * 根据内容类型判断文件扩展名
     *
     * @param contentType 内容类型
     * @return
     */
    public static String getFileexpandedName(String contentType) {
        String fileEndWitsh = "";
        if ("image/jpeg".equals(contentType))
            fileEndWitsh = ".jpg";
        else if ("audio/mpeg".equals(contentType))
            fileEndWitsh = ".mp3";
        else if ("audio/amr".equals(contentType))
            fileEndWitsh = ".amr";
        else if ("video/mp4".equals(contentType))
            fileEndWitsh = ".mp4";
        else if ("video/mpeg4".equals(contentType))
            fileEndWitsh = ".mp4";
        return fileEndWitsh;
    }


    public void sendTemplateMessage(String appid, String secret, WechatTemplate wechatTemplate) {
        try {
            String access_token = this.getWechatToken(appid, secret);
            String url = this.SEND_MESSAGE_URL + access_token;
            String para = JSON.toJSONString(wechatTemplate);
            String resultStr = sendWeCharMsg(para, url);
            log.info("返回结果：" + resultStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送模板信息调用的请求方法
     *
     * @param json
     * @param url
     * @return
     */
    public static String sendWeCharMsg(String json, String url) {
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");
        post.addHeader("Authorization", "Basic YWRtaW46");
        String result = "";
        try {
            StringEntity s = new StringEntity(json, "utf-8");
            s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(s);
            // 发送请求
            HttpResponse httpResponse = HttpClients.createDefault().execute(post);
            // 获取响应输入流
            InputStream inStream = httpResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
            StringBuilder strber = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                strber.append(line + "\n");
            inStream.close();

            result = strber.toString();
            log.info(result);

            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("请求服务器成功，做相应处理");
            } else {
                log.info("请求服务端失败");
            }
        } catch (Exception e) {
            log.error("请求异常");
            throw new RuntimeException(e);
        }
        return result;
    }

}