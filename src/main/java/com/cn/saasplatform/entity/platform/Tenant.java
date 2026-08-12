package com.cn.saasplatform.entity.platform;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.base.BaseEntity;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 租户实体类
 * 继承自BaseEntity，包含租户基本信息
 * 使用@Data注解自动生成getter、setter等方法
 * @TableName("sys_tenant")指定对应的数据库表名为sys_tenant
 */
@Data
@TableName("sys_tenant")
public class Tenant extends BaseEntity {
    // 租户名称
    private String tenantName;
    // 联系人
    private String contact;
    // 联系电话
    private String phone;
    // 套餐ID
    private Long packageId;
    // 到期时间
    private LocalDateTime expireTime;
    // 状态（如：0-禁用，1-正常）
    private Integer status;
}