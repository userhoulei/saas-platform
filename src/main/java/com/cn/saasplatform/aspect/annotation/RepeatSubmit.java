package com.cn.saasplatform.aspect.annotation;

import java.lang.annotation.*;

/**
 * 防重复提交注解
 * 该注解用于标记需要防止重复提交的方法，通过在方法上添加此注解，
 * 可以实现指定时间范围内的提交频率控制，避免用户重复提交相同的数据
 */
@Target(ElementType.METHOD)  // 注解的目标元素为方法
@Retention(RetentionPolicy.RUNTIME)  // 注解的保留策略为运行时，表示注解会在运行时被保留
@Documented  // 表明这个注解将被包含在javadoc中
public @interface RepeatSubmit {

    /**
     * 锁定时间，单位秒，默认2秒内不能重复提交
     * 通过设置lockTime属性，可以自定义防重复提交的时间窗口
     * 例如：设置为5，则表示同一方法在5秒内只能提交一次
     * @return 锁定时间（秒）
     */
    int lockTime() default 2;  // 默认锁定时间为2秒
}