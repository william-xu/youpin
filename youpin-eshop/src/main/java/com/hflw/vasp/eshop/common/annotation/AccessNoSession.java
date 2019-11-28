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
 * @Package com.hflw.vasp.credit.sp.annotation
 * @Description 登陆校验注解  添加注解，无需登录
 * @author liuyf
 * @date 2019年7月13日 下午2:39:08
 * @version
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessNoSession {

    boolean value() default true;

}

