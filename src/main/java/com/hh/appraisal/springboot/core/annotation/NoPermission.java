package com.hh.appraisal.springboot.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 设置此注解的控制器，均不进行权限校验
 * 可配置是否同时不进行登录校验
 * @author gaigai
 * @date 2019/05/15
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoPermission {

    /**
     * 是否同时也不校验登录, ture则不校验登录
     * @return
     */
    boolean noLogin() default false;

}
