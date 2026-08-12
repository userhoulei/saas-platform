package com.cn.saasplatform.util;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class RedissonLockUtil {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 获取分布式锁
     * @param lockKey 锁唯一key
     * @param waitTime 等待获取锁时间
     * @param expireTime 锁自动过期时间
     * @param unit 时间单位
     * @return RLock
     */
    public RLock tryLock(String lockKey, long waitTime, long expireTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean acquire = lock.tryLock(waitTime, expireTime, unit);
            if (acquire) {
                return lock;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /**
     * 释放锁
     */
    public void unLock(RLock lock) {
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * 快捷签到锁：scheduleId+studentId 唯一key
     */
    public RLock getSignLock(Long scheduleId, Long studentId) {
        String key = "edu:sign:lock:" + scheduleId + ":" + studentId;
        return tryLock(key, 3, 10, TimeUnit.SECONDS);
    }
}