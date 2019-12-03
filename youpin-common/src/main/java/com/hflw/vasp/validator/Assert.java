/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.hflw.vasp.validator;

import com.hflw.vasp.exception.ValidateException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 */
public abstract class Assert {

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
