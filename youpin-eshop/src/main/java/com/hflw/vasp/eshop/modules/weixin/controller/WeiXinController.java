package com.hflw.vasp.eshop.modules.weixin.controller;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.hflw.vasp.eshop.common.annotation.AccessNoSession;
import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.eshop.common.exception.ResultCodeEnum;
import com.hflw.vasp.eshop.common.utils.wechat.WechatUtils;
import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.user.service.UserService;
import com.hflw.vasp.eshop.modules.weixin.model.UnifiedOrderModel;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.framework.components.PropertiesUtils;
import com.hflw.vasp.modules.entity.Customer;
import com.hflw.vasp.utils.IpUtils;
import com.hflw.vasp.utils.StringUtils;
import com.hflw.vasp.web.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Zheng Xiajun
 * @ClassName: WxPayController
 * @Description:
 * @date 2018年10月23日
 */
@RestController
@RequestMapping("weixin")
public class WeiXinController extends AbstractController {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private WechatUtils wechatUtils;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(WeiXinController.class);

    /**
     * 获取微信用户openid
     *
     * @param code
     * @param type   0微信公众号openid，1小程序openid
     * @param custId
     * @return
     * @throws Exception
     */
    @AccessNoSession
    @RequestMapping("getWechatOpenId")
    public R getWechatOpenId(String code, Integer type, Long custId) throws Exception {
        Long sessionUserId = getUserId();
        //处理获取公众号openid时候拿不到当期登录用户的情况
        if (sessionUserId == null) sessionUserId = custId;

        String appid = null;
        String secret = null;
        if (Constants.PBULIC == type) {
            appid = PropertiesUtils.getProperty(Constants.WECHAT_APPID);
            secret = PropertiesUtils.getProperty(Constants.WECHAT_SECRET);
        } else if (Constants.MINI == type) {
            appid = PropertiesUtils.getProperty(Constants.WECHAT_MINI_APPID);
            secret = PropertiesUtils.getProperty(Constants.WECHAT_MINI_SECRET);
        }
        logger.info("微信授权码：" + code + ",当前用户id：" + sessionUserId + "--type--" + type + ",session.id:" + session.getId());
        Map<String, Object> wto = wechatUtils.getWechatTokenAndOpenId(appid, secret, code, type);
        if (wto == null || wto.get("openid") == null) {
            return R.error(ResultCodeEnum.ERROR.getCode(), "获取微信用户openid失败");
        }
        String openId = (String) wto.get("openid");
        if (sessionUserId != null) {
            // 关联用户openid
            Customer user = userService.findById(sessionUserId);
            if (user != null) {
                if (Constants.MINI == type) {
                    user.setMiniOpenId(openId);
                    logger.info("当前用户：" + getAccount(), "小程序openid更新完成：" + openId);
                } else if (Constants.PBULIC == type) {
                    user.setWxOpenId(openId);
                    logger.info("当前用户：" + getAccount(), "公众号openid更新完成：" + openId);
                }
                userService.updateByPrimaryKeySelective(user);
            }
        }
        return R.ok().put("openId", openId);
    }


    @RequestMapping("unifiedOrder")
    public R unifiedOrder(UnifiedOrderModel model) throws WxPayException {
        Customer user = userService.getUserByPhone(getAccount());
        if (StringUtils.isNullOrEmpty(user.getWxOpenId()))
            throw BusinessException.create(ResultCodeEnum.USER_NOT_FOLLOW_OFFICIAL_ACCOUNT.getCode(), ResultCodeEnum.USER_NOT_FOLLOW_OFFICIAL_ACCOUNT.getMsg());

        String notifyUrl = PropertiesUtils.getProperty("weixin.repayment.notifyUrl").replace("$userId$", "" + user.getId());
        logger.info("支付成功通知url：" + notifyUrl);
        model.setNotifyUrl(notifyUrl);
        String tradeNo = user.getPhone() + System.currentTimeMillis();
        WxPayMpOrderResult result = unifiedOrder(user.getWxOpenId(), tradeNo, model);
        return R.ok().data(result);
    }

    /**
     * 统一订单
     *
     * @param openid
     * @param tradeNo
     * @param model
     * @return
     * @throws WxPayException
     */
    private WxPayMpOrderResult unifiedOrder(String openid, String tradeNo, UnifiedOrderModel model) throws WxPayException {
        WxPayUnifiedOrderRequest unifiedOrderRequest = WxPayUnifiedOrderRequest.newBuilder()
                .body(model.getBody())
                .totalFee(model.getTotalFee())
                .spbillCreateIp(IpUtils.getIpAddr(request))
                .notifyUrl(model.getNotifyUrl())
                .tradeType(WxPayConstants.TradeType.JSAPI)
                .openid(openid)
                .outTradeNo(tradeNo)
                .limitPay(WxPayConstants.LimitPay.NO_CREDIT)
                .build();
        unifiedOrderRequest.setSignType(WxPayConstants.SignType.MD5);
        return this.wxPayService.createOrder(unifiedOrderRequest);
    }

    /**
     * 微信还款成功回调接口
     *
     * @param userId
     * @return
     */
    @AccessNoSession
    @RequestMapping(value = "wxRepaymentSucc/{userId}", produces = {"application/xml; charset=UTF-8"})
    @ResponseBody
    public String wxRepaymentSucc(@PathVariable("userId") Integer userId) {

        return returnXml("SUCCESS", "OK");
    }

    /**
     * 通过xml 发给微信消息,告诉微信成功收到回调请求
     *
     * @param returnCode
     * @param returnMsg
     * @return
     */
    private String returnXml(String returnCode, String returnMsg) {
        return "<xml><return_code><![CDATA[" + returnCode + "]]></return_code>" +
                "<return_msg><![CDATA[" + returnMsg + "]]></return_msg></xml>";
    }

}
