package com.cn.saasplatform.config.mp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cn.saasplatform.util.TenantContextUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MP自动填充
 * insert的时候自动填充 createTime、tenantId
 * update自动填充updateTime
 * 实体类不需要手动set，简化开发，避免漏填租户ID
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        Long tenantId = TenantContextUtil.getTenantId();
        if (tenantId != null) {
            this.strictInsertFill(metaObject, "tenantId", () -> tenantId, Long.class);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }
}
