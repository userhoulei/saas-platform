package com.cn.saasplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 * 用于配置Spring Data Redis的RedisTemplate实例
 */
@Configuration
public class RedisConfig {

    /**
     * 配置RedisTemplate
     * 设置Redis连接工厂、序列化器等
     *
     * @param factory Redis连接工厂
     * @return 配置好的RedisTemplate实例
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建RedisTemplate实例
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置Redis连接工厂
        template.setConnectionFactory(factory);

        // 创建字符串序列化器，用于key的序列化
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        // 创建JSON序列化器，用于value的序列化
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();

        // 设置key的序列化器
        template.setKeySerializer(keySerializer);
        // 设置value的序列化器
        template.setValueSerializer(valueSerializer);
        // 设置hash结构的key的序列化器
        template.setHashKeySerializer(keySerializer);
        // 设置hash结构的value的序列化器
        template.setHashValueSerializer(valueSerializer);
        // 初始化RedisTemplate
        template.afterPropertiesSet();
        return template;
    }
}