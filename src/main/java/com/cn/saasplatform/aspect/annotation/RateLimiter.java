package com.cn.saasplatform.aspect.annotation;

import java.lang.annotation.*;

/**
 * IP限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /**
     * 限定次数
     */
    int limit() default 10;

    /**
     * 统计周期，单位秒
     */
    int second() default 60;
}