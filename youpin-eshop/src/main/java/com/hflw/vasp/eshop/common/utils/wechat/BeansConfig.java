/**
 * Copyright (C), 2015-2018, 广联赛讯有限公司
 * FileName: BeanConfig
 * Author:   liuyf
 * Date:     2018/5/17 14:50
 * Description: bean配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.hflw.vasp.eshop.common.utils.wechat;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 〈一句话功能简述〉<br>
 * 〈bean配置〉
 *
 * @author liuyf
 * @create 2018/5/17
 * @since 1.0.0
 */
@Configuration
public class BeansConfig {

    @Value("${wechat.youpin.appid}")
    private String appid;

    @Value("${wechat.youpin.mchId}")
    private String mchId;

    @Value("${wechat.youpin.mchKey}")
    private String mchKey;

    @Value("${wechat.youpin.mini.appid}")
    private String miniAppid;

    @Bean
    public WxPayService wxPayService() {
        WxPayService wxPayService = new WxPayServiceImpl();

        //这样配置不支持热更新
        WxPayConfig config = new WxPayConfig();
        config.setAppId(appid);
        config.setMchId(mchId);
        config.setMchKey(mchKey);
        config.setSubAppId(miniAppid);

        wxPayService.setConfig(config);
        return wxPayService;
    }

}