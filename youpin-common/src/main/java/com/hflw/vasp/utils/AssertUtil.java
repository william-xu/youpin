package com.hflw.vasp.utils;

import com.hflw.vasp.exception.ValidateException;
import org.apache.commons.lang.StringUtils;

/**
 * 断言工具类
 */
public class AssertUtil {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new ValidateException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new ValidateException(message);
        }
    }

}
