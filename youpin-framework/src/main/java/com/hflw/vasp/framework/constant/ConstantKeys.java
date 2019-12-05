package com.hflw.vasp.framework.constant;

/**
 * 〈一句话功能简述〉<br>
 * 常量类
 *
 * @author payu
 * @create 3/20/2019 09:46
 * @since 1.0.0
 */
public class ConstantKeys {

    public static final int HOURS_1 = 1;
    public static final int HOURS_24 = 24;
    public static final int DAYS_7 = 7;
    public static final int DAYS_30 = 30;
    public static final int DAYS_365 = 365;

    public static final String ONE_MINUTE_EXPIRE = "OneMinuteCache";
    public static final String ONE_HOUR_EXPIRE = "OneHourCache";
    public static final String ONE_DAY_EXPIRE = "OneDayCache";
    public static final String ONE_MONTH_EXPIRE = "OneMonthCache";
    public static final String ONE_YEAR_EXPIRE = "OneYearCache";

    // 省份
    public static final String REDIS_PROVINCES_KEY = "list:provinces";
    public static final String REDIS_PROVINCE_KEY = "provinces:province_";
    // 市区
    public static final String REDIS_CITIES_KEY = "list:cities";
    public static final String REDIS_CITY_KEY = "cities:city_";
    // 区县
    public static final String REDIS_AREAS_KEY = "list:areas";
    public static final String REDIS_AREA_KEY = "areas:area_";

    /**
     * 微信 公众号
     */
    public static final String WECHAT_APPID = "wechat:appid_";
    public static final String WECHAT_SECRET = "wechat:secret_";

    /**
     * 微信 小程序
     */
    public static final String WECHAT_MINI_APPID = "wechat:mini_appid_";
    public static final String WECHAT_MINI_SECRET = "wechat:mini_secret_";

    /**
     * 微信 token
     */
    public static final String WECHAT_TOKEN_KEY = "wechat:token_";
    public static final String WECHAT_TICKET_KEY = "wechat:ticket_";

    public static final String REDIS_GLOBAL_ORDER_ID = "GLOBAL_ORDER_ID_";

}
