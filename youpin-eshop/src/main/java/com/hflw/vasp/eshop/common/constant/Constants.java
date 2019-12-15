package com.hflw.vasp.eshop.common.constant;

import com.hflw.vasp.constant.BasicConstants;

/**
 * 〈一句话功能简述〉<br>
 * 公共常量类
 *
 * @author liuyf
 * @create 3/20/2019 09:49
 * @since 1.0.0
 */
public final class Constants extends BasicConstants {

    /**
     * 登录用户在SESSION中的KEY值
     */
    public final static String SESSION_LOGIN_USER = "sys_current_user";

    /**
     * 短信验证码前缀key
     */
    public static final String SMS_VERIFY_CODE_PREFIX = "vasp.sms.verifyCode";

    /**
     * 用户openId
     */
    public static final String REDIS_USER_PHONE_KEY = "vasp:user:phone_";

    // 初始化数据有效时间
    public static final long REDIS_INITDATA_VALID_TIME = 30;

    /**
     * 短信验证码过时时间秒数
     */
    public static final int SMS_VERIFY_CODE_TIMEOUT = 300;

    /**
     * 微信 默认公众号
     */
    public static final String WECHAT_APPID = "wechat.appid";
    public static final String WECHAT_SECRET = "wechat.secret";

    /**
     * 微信 小程序
     */
    public static final String WECHAT_MINI_APPID = "wechat.mini.appid";
    public static final String WECHAT_MINI_SECRET = "wechat.mini.secret";

    /**
     * 缓存key
     */
    public static final String WECHAT_TOKEN_KEY = "wechat_token_";
    public static final String WECHAT_TICKET_KEY = "wechat_ticket_";
    public static final String WECHAT_USERINFO_KEY = "wechat_userinfo_";

    /**
     * openid类型
     */
    public static final int PBULIC = 0; //公众号
    public static final int MINI = 1; //小程序

    /**
     * 订单号前缀
     */
    public static final String ORDER_NO_PREFIX = "B";

    /**
     * 审核
     */
    public static final int ENABLE_STATUS_CHECK = 0;
    /**
     * 状态:生效
     */
    public static final int ENABLE_STATUS_EFFECT = 1;

    /**
     * 状态:失效
     */
    public static final int ENABLE_STATUS_INVALID = 2;

    /**
     * 券使用状态：2已使用
     */
    public static final int IS_USED = 2;
    /**
     * 券使用状态：1未使用
     */
    public static final int NOT_USED = 1;

    /**
     * 删除标记  -1：已删除
     */
    public static final int IS_DEL = -1;
    /**
     * 删除标记   0：正常
     */
    public static final int NOT_DEL = 0;

}
