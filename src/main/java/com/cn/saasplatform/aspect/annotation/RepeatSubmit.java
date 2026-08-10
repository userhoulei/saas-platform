package com.cn.saasplatform.aspect.annotation;

import java.lang.annotation.*;

/**
 * 防重复提交注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {

    /**
     * 锁定时间，单位秒，默认2秒内不能重复提交
     */
    int lockTime() default 2;
}