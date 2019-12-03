package com.hflw.vasp.eshop.modules.youpincard.controller;

import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.youpincard.service.YoupinCardService;
import com.hflw.vasp.framework.components.PropertiesUtils;
import com.hflw.vasp.modules.entity.YoupinCard;
import com.hflw.vasp.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * 优品卡
 *
 * @author Mark
 * @date 2019-04-02 10:04:45
 */
@RestController
@RequestMapping("/youpincard")
public class YoupinCardController extends AbstractController {

    @Autowired
    private YoupinCardService youpinCardService;

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
        return R.ok().data(rtm);
    }

    @PostMapping("/active")
    public R active() {
        youpinCardService.active(getUserId());
        return R.ok();
    }

    @PostMapping("/register")
    public R register() {
        String url = PropertiesUtils.getProperty("fsk.register");
        // TODO: 2019/12/3 同步趣融
        return R.ok().data(url);
    }

    @PostMapping("/download")
    public R download() {
        String url = PropertiesUtils.getProperty("fsk.download");
        // TODO: 2019/12/3 同步趣融
        return R.ok().data(url);
    }

}
