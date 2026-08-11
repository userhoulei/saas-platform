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

    /**
     * 插入数据时的填充策略
     * @param metaObject 元数据对象，可以获取到当前正在操作的实体对象信息
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动填充创建时间为当前时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        // 自动填充更新时间为当前时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        // 获取当前租户ID
        Long tenantId = TenantContextUtil.getTenantId();
        // 如果租户ID不为空，则自动填充租户ID
        if (tenantId != null) {
            this.strictInsertFill(metaObject, "tenantId", () -> tenantId, Long.class);
        }
    }

    /**
     * 更新数据时的填充策略
     * @param metaObject 元数据对象，可以获取到当前正在操作的实体对象信息
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动填充更新时间为当前时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }
}
