package com.cn.saasplatform.config.mp;

import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.cn.saasplatform.util.TenantContextUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 多租户配置类
 * 用于配置多租户SQL拦截器，实现数据隔离
 */
@Configuration
public class TenantSqlInterceptor {

    // 需要自动租户过滤的业务数据表
    private static final Set<String> TENANT_TABLES = new HashSet<>(Arrays.asList(
            "sys_user",
            "sys_role",
            "sys_user_role",
            "sys_role_menu",
            "sys_menu",
            "crm_customer"
    ));

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        return new TenantLineInnerInterceptor(new TenantLineHandler() {

            // 重点：返回 Expression 实现类 LongValue，包装Long租户ID
            @Override
            public Expression getTenantId() {
                Long tenantId = TenantContextUtil.getTenantId();
                if (tenantId == null) {
                    return null;
                }
                return new LongValue(tenantId);
            }

            // 数据库租户字段列名
            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            // 忽略表判断逻辑不变
            @Override
            public boolean ignoreTable(String tableName) {
                Long tenantId = TenantContextUtil.getTenantId();
                // 无租户ID / 平台超级管理员(tenantId=0) 跳过租户过滤
                if (tenantId == null || tenantId == 0L) {
                    return true;
                }
                // 不在管控表列表则忽略
                return !TENANT_TABLES.contains(tableName);
            }
        });
    }
}