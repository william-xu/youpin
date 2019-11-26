/**
 *
 */
package com.hflw.vasp.eshop.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @Title AuthCheck.java
 * @Package com.qurong.credit.sp.annotation
 * @Description TODO 登陆校验注解
 * @author qurong
 * @date 2018年7月13日 下午2:39:08
 * @version
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    boolean value() default true;

}

