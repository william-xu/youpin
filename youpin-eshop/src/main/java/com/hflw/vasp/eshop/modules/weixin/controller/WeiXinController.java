package com.hflw.vasp.eshop.modules.weixin.controller;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.hflw.vasp.annotation.SysLog;
import com.hflw.vasp.eshop.common.annotation.AccessNoSession;
import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.eshop.common.exception.ResultCodeEnum;
import com.hflw.vasp.eshop.common.utils.wechat.WechatPayUtil;
import com.hflw.vasp.eshop.common.utils.wechat.WechatUtils;
import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.weixin.model.UnifiedOrderModel;
import com.hflw.vasp.eshop.modules.youpincard.service.YoupinCardService;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.framework.components.PropertiesUtils;
import com.hflw.vasp.modules.entity.Customer;
import com.hflw.vasp.utils.IpUtils;
import com.hflw.vasp.utils.SnowFlake;
import com.hflw.vasp.utils.StringUtils;
import com.hflw.vasp.web.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zheng Xiajun
 * @ClassName: WxPayController
 * @Description:
 * @date 2019年10月23日
 */
@RestController
@RequestMapping("weixin")
public class WeiXinController extends AbstractController {

    private static final Logger logger = LoggerFactory.getLogger(WeiXinController.class);

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private WechatUtils wechatUtils;

    @Autowired
    private YoupinCardService youpinCardService;

    /**
     * 获取微信用户openid
     *
     * @param code
     * @param type 0微信公众号openid，1小程序openid
     * @return
     * @throws Exception
     */
    @SysLog
    @AccessNoSession
    @RequestMapping("getWechatOpenId")
    public R getWechatOpenId(String code, Integer type) throws Exception {
        String appid = null;
        String secret = null;
        if (Constants.PBULIC == type) {
            appid = PropertiesUtils.getProperty(Constants.WECHAT_APPID);
            secret = PropertiesUtils.getProperty(Constants.WECHAT_SECRET);
        } else if (Constants.MINI == type) {
            appid = PropertiesUtils.getProperty(Constants.WECHAT_MINI_APPID);
            secret = PropertiesUtils.getProperty(Constants.WECHAT_MINI_SECRET);
        }
        logger.info("微信授权码：" + code + ",--type--" + type);
        Map<String, Object> wto = wechatUtils.getWechatTokenAndOpenId(appid, secret, code, type);
        if (wto == null || wto.get("openid") == null) {
            return R.error(ResultCodeEnum.ERROR.getCode(), "获取微信用户openid失败");
        }
        String openId = (String) wto.get("openid");
        return R.ok().data(openId);
    }

    @SysLog
    @PostMapping("unifiedOrder")
    public R unifiedOrder(UnifiedOrderModel model) throws WxPayException {
        Customer user = getSessionUser();
        if (StringUtils.isNullOrEmpty(user.getWxOpenId()))
            throw BusinessException.create(ResultCodeEnum.USER_NOT_FOLLOW_OFFICIAL_ACCOUNT.getCode(), ResultCodeEnum.USER_NOT_FOLLOW_OFFICIAL_ACCOUNT.getMsg());

        String notifyUrl = PropertiesUtils.getProperty("wechat.repayment.notifyUrl").replace("$userId$", "" + user.getId());
        logger.info("支付成功通知url：" + notifyUrl);
        model.setNotifyUrl(notifyUrl);

        String tradeNo = "";
        if (model.getType() == 1) {
            tradeNo = "YP" + SnowFlake.nextSerialNumber();
            // TODO: 2019/12/5 交易记录入库
        } else {
            tradeNo = SnowFlake.nextSerialNumber();
        }
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
     * 微信支付回调接口
     *
     * @param userId
     * @return
     */
    @SysLog
    @AccessNoSession
    @RequestMapping(value = "callBackWXpay/{userId}", produces = {"application/xml; charset=UTF-8"})
    @ResponseBody
    public String callBackWXpay(@PathVariable("userId") Long userId) {

        logger.info("================================================开始处理微信小程序发送的异步通知");

        //1 获取微信支付异步回调结果
        String xmlResult = WechatPayUtil.getPostStr(request);

        Map<String, String> resultMap = null;
        try {
            //将结果转成map
            resultMap = WechatPayUtil.xmlToMap(xmlResult);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //订单号
        String orderNo = resultMap.get("out_trade_no");
        logger.info("订单号：------------------" + orderNo + "结束----------");
        String result_code = resultMap.get("result_code");
        //回调返回的加密签名 保存下来 下面会进行对比
        String sign = resultMap.get("sign");
        //去掉sign和利用微信回调回来的信息重新加密
        resultMap.remove("sign");
        String sign1 = "";
        try {
            //重新加密 获取加密的签名
            sign1 = WechatPayUtil.generateSignature(resultMap, wxPayService.getConfig().getMchKey()); //签名
        } catch (Exception e) {

        }

        String resultCode;
        String resultMsg;
        //对比微信回调的加密与重新加密是否一致  一致即为通过 不一致说明被改动过 加密不通过
        logger.info("==============================================开始对比加密++++++++++++++++++++++++++++++++++++++");
        if (sign.equals(sign1)) { //验签通过
            logger.info("==============================================验签通过++++++++++++++++++++++++++++++++++++++");

            if (WxPayConstants.ResultCode.SUCCESS.equalsIgnoreCase(result_code)) {//业务结果为SUCCESS
                /**
                 * 待处理的逻辑：
                 */
                youpinCardService.active(userId);

                resultCode = "SUCCESS";
                resultMsg = "成功";
            } else { // 业务结果为FALL
                resultCode = "FAIL";
                resultMsg = "业务结果为FAIL";
            }

        } else {
            resultCode = "FAIL";
            resultMsg = "验签未通过";
        }

        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("return_code", resultCode);
        returnMap.put("return_msg", resultMsg);
        try {
            return WechatPayUtil.mapToXml(returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
