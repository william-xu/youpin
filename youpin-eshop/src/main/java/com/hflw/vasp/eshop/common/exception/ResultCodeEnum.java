package com.hflw.vasp.eshop.common.exception;

/**
 * @author qurong
 * @Title ResultCodeEnum.java
 * @Package com.qurong.constants
 * @Description 结果集
 * @date 2018年10月22日 上午10:51:53
 */

public enum ResultCodeEnum {

    /**
     * 系统相关
     */
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    ERROR(202, "系统异常"),
    PARAM_ERROR(203, "参数错误"),
    NOT_LOGIN(204, "用户未登录"),
    NOT_REDIS_LOGIN(205, "用户未登录，Redis缓存不存在"),
    NOT_SESSION_LOGIN(205, "用户未登录，Session不存在"),
    NOT_MINIOPENID_LOGIN(205, "用户未登录，MiniOpenid不存在或不一致"),
    TRY_AGAIN(206, "失败，请重试"),
    FILE_NULL(207, "上传文件为空"),
    FILE_SIZE_TOO_BIG(208, "上传文件超过限制"),
    USER_NOT_EXIST(209, "用户不存在"),

    //登录相关
    USERNAME_NOT_EXIST(300, "手机号不存在"),
    SMS_VERIFY_CODE_TIMEOUT(301, "短信验证码已过期"),
    SMS_VERIFY_CODE_NOT_RIGHT(302, "短信验证码错误"),
    SMS_VERIFY_CODE_SEND_FAIL(303, "短信验证码发送失败"),

    /***************************业务相关******************************/
    //门店
    STORE_NOT_EXIST(600, "此手机号码没有分配对应的门店账号如需新增门店请点击“下一步”继续"),
    STORE_IS_EXIST_PHONE(601, "此手机号对应的门店账号已存在，请联系SP"),
    STORE_IS_CHECK(602, "该手机号码绑定的门店处于审核中,请耐心等待"),
    STORE_IS_STOP(603, "该手机号码绑定的门店处于停用状态，请联系SP"),
    STORE_IS_STOP_NAME(604, "已存在相同名称的门店，请联系SP"),
    STORE_IS_DEL(605, "手机号对应的门店已删除，请联系SP"),
    //省市数据
    PROVINCE_NOT_EXIST(700, "省份不存在"),
    CITY_NOT_EXIST(701, "城市不存在"),
    AREAS_NOT_EXIST(702, "区域不存在");

    /**
     * 状态码
     */
    private int code;
    /**
     * 状态码对应的信息
     */
    private String msg;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
