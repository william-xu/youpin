package com.hflw.vasp.exception;

/**
 * 〈一句话功能简述〉<br>
 * 验证类异常
 *
 * @author payu
 * @create 3/19/2019 16:40
 * @since 1.0.0
 */
public class ValidateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 600;

    public ValidateException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ValidateException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public ValidateException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public ValidateException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
