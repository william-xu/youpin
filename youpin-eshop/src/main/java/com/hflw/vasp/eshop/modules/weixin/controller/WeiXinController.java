package com.hflw.vasp.eshop.modules.weixin.controller;

import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.hflw.vasp.annotation.AccessNoSession;
import com.hflw.vasp.annotation.SysLog;
import com.hflw.vasp.eshop.common.constant.Constants;
import com.hflw.vasp.eshop.common.exception.ResultCodeEnum;
import com.hflw.vasp.eshop.common.utils.wechat.WechatPayUtil;
import com.hflw.vasp.eshop.common.utils.wechat.WechatUtils;
import com.hflw.vasp.eshop.modules.AbstractController;
import com.hflw.vasp.eshop.modules.order.service.OrderService;
import com.hflw.vasp.eshop.modules.trading.service.TradingService;
import com.hflw.vasp.eshop.modules.weixin.model.UnifiedOrderModel;
import com.hflw.vasp.eshop.modules.youpincard.service.YoupinCardService;
import com.hflw.vasp.exception.BusinessException;
import com.hflw.vasp.framework.components.PropertiesUtils;
import com.hflw.vasp.modules.entity.Customer;
import com.hflw.vasp.modules.entity.Order;
import com.hflw.vasp.modules.entity.TradingFlow;
import com.hflw.vasp.utils.IpUtils;
import com.hflw.vasp.utils.SnowFlake;
import com.hflw.vasp.utils.StringUtils;
import com.hflw.vasp.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zheng Xiajun
 * @ClassName: WxPayController
 * @Description:
 * @date 2019年10月23日
 */
@Slf4j
@RestController
@RequestMapping("weixin")
public class WeiXinController extends AbstractController {

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private WechatUtils wechatUtils;

    @Autowired
    private YoupinCardService youpinCardService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TradingService tradingService;

    /**
     * 获取微信Appid
     *
     * @return
     */
    @AccessNoSession
    @RequestMapping("getWechatAppid")
    public R getWechatAppid() {
        Integer type = Constants.PBULIC;
        String appid = "";
        if (Constants.PBULIC == type) {
            appid = PropertiesUtils.getProperty(Constants.WECHAT_APPID);
        } else if (Constants.MINI == type) {
            appid = PropertiesUtils.getProperty(Constants.WECHAT_MINI_APPID);
        }
        return R.ok().data(appid);
    }

    /**
     * 微信openId置为空
     */
    @SysLog
    @GetMapping("getUserInfo")
    @ResponseBody
    public R getUserInfo(String openId) throws Exception {
        String appid = PropertiesUtils.getProperty(Constants.WECHAT_APPID);
        String secret = PropertiesUtils.getProperty(Constants.WECHAT_SECRET);
        Map<String, Object> wto = wechatUtils.getWechatUserInfo(appid, secret, openId);
        return R.ok().data(wto);
    }

    /**
     * 获取微信Appid
     *
     * @return
     */
    @AccessNoSession
    @GetMapping("getWechatConfig")
    public R getWechatConfig(String url) throws Exception {
        Integer type = Constants.PBULIC;
        String appid = "";
        String secret = "";
        if (Constants.PBULIC == type) {
            appid = PropertiesUtils.getProperty(Constants.WECHAT_APPID);
            secret = PropertiesUtils.getProperty(Constants.WECHAT_SECRET);
        } else if (Constants.MINI == type) {
            appid = PropertiesUtils.getProperty(Constants.WECHAT_MINI_APPID);
            secret = PropertiesUtils.getProperty(Constants.WECHAT_MINI_SECRET);
        }
        Map<String, String> ret = wechatUtils.sign(appid, secret, url);
        return R.ok().data(ret);
    }

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
    @GetMapping("getWechatOpenId")
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
        log.info("微信授权码：" + code + ",--type--" + type);
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

        if (model.getTotalFee() == null || model.getTotalFee() <= 0)
            throw BusinessException.create("支付金额有误或此订单无需支付");

        String notifyUrl = PropertiesUtils.getProperty("wechat.repayment.notifyUrl");
        log.info("支付通知url：" + notifyUrl);
        model.setNotifyUrl(notifyUrl);

        String tradeFlowNo = "";
        if (model.getType() == 1) {
            tradeFlowNo = "YP" + SnowFlake.nextSerialNumber();
        } else {
            tradeFlowNo = "B" + SnowFlake.nextSerialNumber();
        }

        WxPayMpOrderResult result = unifiedOrder(user.getWxOpenId(), tradeFlowNo, model);

        //优品卡交易记录|商品购买交易记录入库
        TradingFlow tradingFlow = new TradingFlow();
        tradingFlow.setFlowNo(tradeFlowNo);
        tradingFlow.setOrderId(model.getOrderId());
        tradingFlow.setBody(model.getBody());
        tradingFlow.setType(0);
        tradingFlow.setStatus(0);
        tradingFlow.setCreateTime(new Date());
        tradingService.save(tradingFlow);

        return R.ok().data(result);
    }

    /**
     * 统一订单
     *
     * @param openid
     * @param tradeFlowNo
     * @param model
     * @return
     * @throws WxPayException
     */
    private WxPayMpOrderResult unifiedOrder(String openid, String tradeFlowNo, UnifiedOrderModel model) throws WxPayException {
        WxPayUnifiedOrderRequest unifiedOrderRequest = WxPayUnifiedOrderRequest.newBuilder()
                .body(model.getBody())
                .totalFee(model.getTotalFee())
                .spbillCreateIp(IpUtils.getIpAddr(request))
                .notifyUrl(model.getNotifyUrl())
                .tradeType(WxPayConstants.TradeType.JSAPI)
                .openid(openid)
                .outTradeNo(tradeFlowNo)
                .limitPay(WxPayConstants.LimitPay.NO_CREDIT)
                .build();
        unifiedOrderRequest.setSignType(WxPayConstants.SignType.MD5);
        return this.wxPayService.createOrder(unifiedOrderRequest);
    }

    /**
     * 微信支付回调接口
     *
     * @return
     */
    @SysLog
    @AccessNoSession
    @RequestMapping(value = "callBackWXpay", produces = {"application/xml; charset=UTF-8"})
    @ResponseBody
    public String callBackWXpay() {

        log.info("================================================开始处理微信支付发送的异步通知");

        //1 获取微信支付异步回调结果
        String xmlResult = WechatPayUtil.getPostStr(request);
        log.info("异步通知参数：" + xmlResult);

        Map<String, String> resultMap = new HashMap<>();
        try {
            //将结果转成map
            resultMap = WechatPayUtil.xmlToMap(xmlResult);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        //订单号
        String outTradeNo = resultMap.get("out_trade_no");
        log.info("交易流水号：------------------" + outTradeNo + "结束----------");
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
            e.printStackTrace();
        }

        String resultCode;
        String resultMsg;
        //对比微信回调的加密与重新加密是否一致  一致即为通过 不一致说明被改动过 加密不通过
        log.info("==============================================开始对比加密++++++++++++++++++++++++++++++++++++++");
        if (StringUtils.isNotEmpty(sign) && sign.equals(sign1)) { //验签通过
            log.info("==============================================验签通过++++++++++++++++++++++++++++++++++++++");
            if (WxPayConstants.ResultCode.SUCCESS.equalsIgnoreCase(result_code)) {//业务结果为SUCCESS
                log.info("查询交易流水：{}", outTradeNo);
                //查询订单交易记录，修改交易状态
                TradingFlow tradingFlow = tradingService.findByFlowNo(outTradeNo);
                if (tradingFlow != null) {
                    log.info("查询订单ID：{}", tradingFlow.getOrderId());
                    Order order = orderService.findById(tradingFlow.getOrderId());
                    //区分优品卡还是商品订单
                    if (order.getType() == 1) {
//                        1、优品卡，查询查询优品卡记录，修改激活状态
                        // 查赠送商品订单
                        Order subOrder = orderService.findByParentOrderNo(order.getOrderNo());
                        if (subOrder != null) {
                            log.info("更新优品卡订单{}附属订单{}状态为已支付", subOrder.getParentOrderNo(), subOrder.getOrderNo());
                            subOrder.setStatus(1);
                            orderService.update(subOrder);
                        }
                        //激活优品卡
                        log.info("激活用户{}优品卡权益", order.getUserId());
                        youpinCardService.active(order.getUserId());
                    } else {
//                        2、商品，查询订单记录，修改订单状态
                    }
                    //修改订单状态为已支付
                    log.info("更新订单{}状态为已支付", order.getOrderNo());
                    order.setStatus(1);
                    orderService.update(order);

                    tradingFlow.setStatus(1);
                    tradingService.save(tradingFlow);
                }
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
