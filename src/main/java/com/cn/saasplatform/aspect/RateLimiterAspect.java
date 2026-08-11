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

/**
 * 限流切面类，用于实现基于IP的访问频率控制
 * 使用Redis作为计数器存储，实现分布式限流
 */
@Aspect
@Component
public class RateLimiterAspect {

    // Redis键的前缀，用于区分不同类型的键
    private static final String PREFIX = "rate_limit:";

    // 注入Redis模板，用于操作Redis
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 环绕通知，在带有@RateLimiter注解的方法执行前后进行限流控制
     * @param point 连接点，可以获取目标方法的信息
     * @param rateLimiter 限流注解，包含限流参数
     * @return 目标方法的执行结果
     * @throws Throwable 可能抛出的异常
     */
    @Around("@annotation(rateLimiter)")
    public Object around(ProceedingJoinPoint point, RateLimiter rateLimiter) throws Throwable {
        // 获取当前请求的属性
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 获取当前请求对象
        HttpServletRequest request = attributes.getRequest();

        // 获取客户端IP地址
        String ip = getIpAddr(request);
        // 构建Redis键，使用IP作为标识
        String key = PREFIX + ip;



        // 从注解中获取限流参数
        int limit = rateLimiter.limit();  // 限流阈值
        int second = rateLimiter.second(); // 时间窗口（秒）

        // 计数器自增
        Long count = redisTemplate.opsForValue().increment(key, 1);
        // 如果是第一次设置，则设置过期时间
        if (count == 1) {
            redisTemplate.expire(key, second, TimeUnit.SECONDS);
        }

        // 如果超过限流阈值，则抛出异常
        if (count > limit) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "访问过于频繁，请稍后再试");
        }

        // 放行，执行目标方法
        return point.proceed();
    }

    /**
     * 获取客户端真实IP地址
     * 优先从请求头中获取，如果获取不到则使用远程地址
     * @param request HTTP请求对象
     * @return 客户端IP地址
     */
    private String getIpAddr(HttpServletRequest request) {
        // 从x-forwarded-for头获取IP
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // 从Proxy-Client-IP头获取IP
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // 从WL-Proxy-Client-IP头获取IP
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // 使用远程地址作为IP
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}