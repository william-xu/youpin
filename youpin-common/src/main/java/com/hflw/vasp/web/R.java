package com.hflw.vasp.web;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 */
public class R extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    private final static String CODE_KEY = "code";
    private final static String MSG_KEY = "msg";
    private final static String DATA_KEY = "data";
    private final static String LIST_KEY = "list";
    private final static String PAGE_KEY = "page";
    private final static String TOTAL_KEY = "total";

    public R() {
        put(CODE_KEY, 200);
        put(MSG_KEY, "success");
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put(CODE_KEY, code);
        r.put(MSG_KEY, msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put(MSG_KEY, msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R data(Object o) {
        R r = new R();
        r.put(DATA_KEY, o);
        return r;
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    /**
     * 分页信息
     *
     * @param value
     * @param total
     * @return
     */
    public R putPageData(Object value, long total) {
        Map data = new HashMap(2);
        data.put(PAGE_KEY, value);
        data.put(TOTAL_KEY, total);
        super.put(DATA_KEY, data);
        return this;
    }

    public void setMessage(String formatMessage) {
        this.put(MSG_KEY, formatMessage);
    }

    public String getMessage() {
        return (String) this.get(MSG_KEY);
    }

    public void setCode(int code) {
        this.put(CODE_KEY, code);
    }

    public int getCode() {
        return (int) this.get(CODE_KEY);
    }
}
