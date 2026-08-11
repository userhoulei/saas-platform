package com.cn.saasplatform.aspect;

import cn.hutool.core.util.StrUtil;
import com.cn.saasplatform.aspect.annotation.OperateLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 操作日志AOP切面
 * 使用@Around环绕通知，可以获取【方法执行前、执行后、异常、耗时】
 * 无侵入式日志，只需要在controller方法上加注解即可收集操作日志
 * 需要操作审计
 * 后续可以扩展：存入数据库，当前阶段先打印日志，后期持久化
 */
@Slf4j  // Lombok注解，用于自动生成日志对象
@Aspect  // 声明为切面类
@Component  // 声明为Spring组件
public class OperateLogAspect {

    // 切点：所有标记 @OperateLog 的方法
    @Pointcut("@annotation(com.cn.saasplatform.aspect.annotation.OperateLog)")  // 定义切点，匹配带有@OperateLog注解的方法
    public void logPointCut() {
    }

    @Around("logPointCut()")  // 环绕通知，在切点方法执行前后执行
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();  // 记录方法开始时间
        MethodSignature signature = (MethodSignature) point.getSignature();  // 获取方法签名
        Method method = signature.getMethod();  // 获取方法对象
        OperateLog annotation = method.getAnnotation(OperateLog.class);  // 获取方法上的@OperateLog注解
        String operateDesc = annotation.value();  // 获取注解中的操作描述

        // 获取request对象
        RequestAttributes requestAttr = RequestContextHolder.getRequestAttributes();  // 获取请求属性
        HttpServletRequest request = ((ServletRequestAttributes) requestAttr).getRequest();  // 获取HttpServletRequest对象
        String url = request.getRequestURI();  // 获取请求URL
        String ip = getIp(request);  // 获取客户端IP地址

        try {
            Object result = point.proceed();  // 执行目标方法
            long cost = System.currentTimeMillis() - start;  // 计算方法执行耗时
            // 打印操作日志
            log.info("[操作日志] 描述:{},URL:{},IP:{},耗时:{}ms", operateDesc, url, ip, cost);
            return result;  // 返回方法执行结果
        } catch (Throwable e) {  // 捕获异常
            // 打印操作异常日志
            log.error("[操作异常] 描述:{},URL:{},IP:{}", operateDesc, url, ip, e);
            throw e;  // 抛出异常
        }
    }

    /**
     * 获取客户端IP地址
     * @param request HttpServletRequest对象
     * @return 客户端IP地址
     */
    private String getIp(HttpServletRequest request) {
        // 获取X-Forwarded-For头信息
        String xff = request.getHeader("X-Forwarded-For");
        // 如果X-Forwarded-For不为空，则返回第一个IP
        if (StrUtil.isNotBlank(xff)) {
            return xff.split(",")[0].trim();
        }
        // 否则返回远程地址
        return request.getRemoteAddr();
    }
}