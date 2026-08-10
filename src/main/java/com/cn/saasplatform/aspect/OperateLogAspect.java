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
@Slf4j
@Aspect
@Component
public class OperateLogAspect {

    // 切点：所有标记 @OperateLog 的方法
    @Pointcut("@annotation(com.cn.saasplatform.aspect.annotation.OperateLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        OperateLog annotation = method.getAnnotation(OperateLog.class);
        String operateDesc = annotation.value();

        // 获取request
        RequestAttributes requestAttr = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttr).getRequest();
        String url = request.getRequestURI();
        String ip = getIp(request);

        try {
            Object result = point.proceed();
            long cost = System.currentTimeMillis() - start;
            log.info("[操作日志] 描述:{},URL:{},IP:{},耗时:{}ms", operateDesc, url, ip, cost);
            return result;
        } catch (Throwable e) {
            log.error("[操作异常] 描述:{},URL:{},IP:{}", operateDesc, url, ip, e);
            throw e;
        }
    }

    private String getIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        if (StrUtil.isNotBlank(xff)) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}