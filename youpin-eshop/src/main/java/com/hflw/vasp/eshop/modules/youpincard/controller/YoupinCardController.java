package com.hflw.vasp.eshop.modules.youpincard.controller;

import cn.hutool.http.HttpUtil;
import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.order.service.OrderService;
import com.hflw.vasp.eshop.modules.youpincard.model.FushikangLinkModel;
import com.hflw.vasp.eshop.modules.youpincard.service.YoupinCardService;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.framework.components.PropertiesUtils;
import com.hflw.vasp.modules.entity.Order;
import com.hflw.vasp.modules.entity.YoupinCard;
import com.hflw.vasp.utils.SignUtils;
import com.hflw.vasp.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * 优品卡
 *
 * @author Mark
 * @date 2019-04-02 10:04:45
 */
@Slf4j
@RestController
@RequestMapping("/youpincard")
public class YoupinCardController extends AbstractController {

    @Autowired
    private YoupinCardService youpinCardService;

    @Autowired
    private OrderService orderService;

    /**
     * 优品卡信息
     */
    @GetMapping("/info")
    public R info() {
        //优品卡
        YoupinCard card = youpinCardService.findByUserId(getUserId());

        //有效判断
        boolean flag = youpinCardService.verifyValid(card);

        Map<String, Object> rtm = new HashMap<>();
        rtm.put("card", card);
        rtm.put("flag", flag);

        if (flag) {
            FushikangLinkModel flm = new FushikangLinkModel();
            flm.setRegister(PropertiesUtils.getProperty("fsk.register"));
            flm.setDownload(PropertiesUtils.getProperty("fsk.download"));
            rtm.put("links", flm);
        }
        return R.ok().data(rtm);
    }

    @PostMapping("/active")
    public R active() {
        youpinCardService.active(getUserId());
        return R.ok();
    }

    @GetMapping("/link")
    public R link(Integer type) throws Exception {
        String url = "";
        if (type == 1) {
            url = PropertiesUtils.getProperty("fsk.register");
        } else if (type == 2) {
            url = PropertiesUtils.getProperty("fsk.download");
        }
        log.info("用户{}点击了{}", getAccount(), url);

        //2019/12/3 同步趣融
        String appKey = PropertiesUtils.getProperty("fsk.tcw.appKey");
        String appSecret = PropertiesUtils.getProperty("fsk.tcw.appSecret");

        Order order = orderService.findValidYoupinOrder(getUserId());
        if (order == null)
            throw BusinessException.create("优品卡订单未生效！");

        Map<String, String> tparams = new TreeMap<>();
        tparams.put("appKey", appKey);
        tparams.put("merchantId", order.getPromoCode());
        tparams.put("name", "");
        tparams.put("phone", getAccount());
        tparams.put("opType", String.valueOf(type));
        String sign = SignUtils.sign(tparams, appSecret);
        log.info("Sign:" + sign);
        tparams.put("sign", sign);

        Map<String, Object> params = new HashMap<>();
        params.putAll(tparams);

        String qurongUrl = PropertiesUtils.getProperty("qurong.admin.url");
        String result = HttpUtil.post(qurongUrl, params);
        log.info("Qurong result:" + result);

        return R.ok().data(url);
    }

}
