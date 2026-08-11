package com.cn.saasplatform.aspect.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解：RateLimiter，用于实现接口限流功能
 * 该注解可以应用于方法上，运行时保留，并被包含在JavaDoc中
 */
@Target(ElementType.METHOD)  // 指定该注解只能应用于方法上
@Retention(RetentionPolicy.RUNTIME)  // 指定该注解在运行时仍然保留
@Documented  // 表示该注解会被包含在JavaDoc中
public @interface RateLimiter {
    // 周期内最大访问次数，默认值为10
    int limit() default 10;
    // 统计周期 秒
    int second() default 60;
}