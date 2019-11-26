package com.hflw.vasp.enums;

/**
 * 用于注解  @SysLogMark
 * 保存参数方式
 */
public enum RequestSaveMethod {
    // 从 Request 对象中拿,用 requet.getParamMap
    REQUEST,
    //使用反射从方法参数中拿
    REFLECT
}
