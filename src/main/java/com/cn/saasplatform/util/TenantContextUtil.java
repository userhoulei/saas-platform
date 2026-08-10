package com.cn.saasplatform.util;

/**
 * 租户上下文 ThreadLocal
 * ThreadLocal 保存单次请求的租户ID；
 * 同一个请求所有service/mapper都可以直接获取租户ID；
 * ⚠️重点：必须finally清除，防止线程池复用引发数据错乱（内存泄漏）
 */
public class TenantContextUtil {

    private static final ThreadLocal<Long> TENANT_HOLDER = new ThreadLocal<>();

    public static void setTenantId(Long tenantId) {
        TENANT_HOLDER.set(tenantId);
    }

    public static Long getTenantId() {
        return TENANT_HOLDER.get();
    }

    public static void clear() {
        TENANT_HOLDER.remove();
    }
}