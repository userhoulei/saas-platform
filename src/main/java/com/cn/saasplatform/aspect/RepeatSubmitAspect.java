package com.cn.saasplatform.aspect;

import com.cn.saasplatform.aspect.annotation.RepeatSubmit;
import com.cn.saasplatform.exception.BusinessException;
import com.cn.saasplatform.entity.resp.ResultCode;
import com.cn.saasplatform.util.TenantContextUtil;
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
public class RepeatSubmitAspect {

    // Redis中防止重复提交的key前缀
    private static final String PREFIX = "repeat_submit:";

    // Redis模板，用于操作Redis
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 环绕通知，用于处理防止重复提交的逻辑
     * @param point 连接点，可以执行目标方法
     * @param repeatSubmit 注解，包含锁定的时长
     * @return 目标方法的执行结果
     * @throws Throwable 可能抛出的异常
     */
    @Around("@annotation(repeatSubmit)")
    public Object around(ProceedingJoinPoint point, RepeatSubmit repeatSubmit) throws Throwable {
        // 获取当前请求的属性和请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 组装唯一key: 租户ID + 用户IP + 请求URI
        Long tenantId = TenantContextUtil.getTenantId();
        String ip = getIpAddr(request);
        String uri = request.getRequestURI();
        String key = PREFIX + tenantId + ":" + ip + ":" + uri;

        // 检查Redis中是否存在该key，如果存在则说明是重复提交
        Boolean exists = redisTemplate.hasKey(key);
        if (Boolean.TRUE.equals(exists)) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "请勿重复提交请求");
        }

        // 设置过期时间
        int lockTime = repeatSubmit.lockTime();
        redisTemplate.opsForValue().set(key, System.currentTimeMillis(), lockTime, TimeUnit.SECONDS);

        try {
            return point.proceed();
        } finally {
            // 执行完毕可以主动删key，也可以等自动过期
            // redisTemplate.delete(key);
        }
    }

    /**
     * 获取客户端IP
     */
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