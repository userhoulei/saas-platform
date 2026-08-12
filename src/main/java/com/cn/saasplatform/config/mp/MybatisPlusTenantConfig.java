package com.cn.saasplatform.config.mp;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.cn.saasplatform.util.TenantContextUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * MyBatis-Plus 多租户自动拦截配置
 * 自动给指定表拼接 tenant_id 条件，sys_menu 全局忽略
 */
@Configuration
public class MybatisPlusTenantConfig {

    /**
     * 需要开启租户自动过滤的表
     * sys_menu 全局平台菜单，不加入此列表，由业务套餐手动过滤
     */
    private static final Set<String> TENANT_CONTROL_TABLES = new HashSet<>(Arrays.asList(
            "sys_user",
            "sys_role",
            "sys_user_role",
            "sys_role_menu",
            "sys_tenant",
            "sys_tenant_package",
            "crm_customer"
    ));

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        return new TenantLineInnerInterceptor(new TenantLineHandler() {

            /**
             * 获取当前租户ID
             * null兜底为平台租户0
             */
            @Override
            public Expression getTenantId() {
                Long tenantId = TenantContextUtil.getTenantId();
                Long finalTenantId = (tenantId == null) ? 0L : tenantId;
                return new LongValue(finalTenantId);
            }

            /**
             * 数据库租户字段名称
             */
            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            /**
             * 是否忽略当前表的租户条件
             * true = 忽略，不拼接tenant_id
             * false = 启用，自动拼接tenant_id = ?
             */
            @Override
            public boolean ignoreTable(String tableName) {
                Long tenantId = TenantContextUtil.getTenantId();
                // 1. 平台超级管理员 直接忽略所有表，查询全量数据
                if (tenantId != null && tenantId == 0L) {
                    return true;
                }
                // 2. 普通租户：只有在管控列表内的表才启用租户拦截，其他全部忽略
                return !TENANT_CONTROL_TABLES.contains(tableName);
            }
        });
    }
}