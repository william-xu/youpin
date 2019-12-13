package com.hflw.vasp.admin.common.exception;

/**
 * @author liuyf
 * @Title ResultCodeEnum.java
 * @Package com.hflw.vasp.constants
 * @Description 结果集
 * @date 2019年10月22日 上午10:51:53
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
    NOT_USER_EXIST(205, "免登录用户不存在"),
    TRY_AGAIN(206, "失败，请重试"),
    FILE_NULL(207, "上传文件为空"),
    FILE_SIZE_TOO_BIG(208, "上传文件超过限制"),

    //登录相关
    USERNAME_OR_PASSWORD_NOT_RIGHT(300, "用户名或密码错误"),
    VERIFY_CODE_TIMEOUT(301, "验证码已过期"),
    VERIFY_CODE_NOT_RIGHT(302, "验证码错误"),
    /***************************业务相关******************************/

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
