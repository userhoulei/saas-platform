package com.cn.saasplatform.aspect.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解：用于标记需要记录操作日志的方法
 * 该注解可以应用于方法级别，在运行时保留，并且会被包含在JavaDoc中
 */
@Target(ElementType.METHOD) // 指定该注解只能应用于方法上
@Retention(RetentionPolicy.RUNTIME) // 指定该注解在运行时仍然保留
@Documented // 指定该注解会被包含在JavaDoc中
public @interface OperateLog {
    /** 操作描述 */
    String value() default "";
}