package com.cn.saasplatform.config.mp;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * MyBatis-Plus配置类
 * 用于配置MyBatis-Plus的拦截器，包括多租户拦截器和分页插件
 */
@Configuration
public class MybatisPlusConfig {

    @Resource
    private MybatisPlusTenantConfig tenantSqlInterceptor; // 注入多租户SQL拦截器

    /**
     * 配置MyBatis-Plus拦截器
     * @return MybatisPlusInterceptor 配置好的拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 多租户拦截器放在第一位，确保优先执行
        interceptor.addInnerInterceptor(tenantSqlInterceptor.tenantLineInnerInterceptor());
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
