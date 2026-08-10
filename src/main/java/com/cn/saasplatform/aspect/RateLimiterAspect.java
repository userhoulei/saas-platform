package com.cn.saasplatform.aspect;

import com.cn.saasplatform.aspect.annotation.RateLimiter;
import com.cn.saasplatform.exception.BusinessException;
import com.cn.saasplatform.entity.resp.ResultCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RateLimiterAspect {

    private static final String PREFIX = "rate_limit:";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(rateLimiter)")
    public Object around(ProceedingJoinPoint point, RateLimiter rateLimiter) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String ip = getIpAddr(request);
        String key = PREFIX + ip;

        int limit = rateLimiter.limit();
        int second = rateLimiter.second();

        // 计数器自增
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (count == 1) {
            redisTemplate.expire(key, second, TimeUnit.SECONDS);
        }

        if (count > limit) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "访问过于频繁，请稍后再试");
        }

        return point.proceed();
    }

    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}