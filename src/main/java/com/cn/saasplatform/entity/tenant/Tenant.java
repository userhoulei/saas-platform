package com.cn.saasplatform.entity.tenant;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("sys_tenant")
public class Tenant extends BaseEntity {
    private String tenantName;
    private String contact;
    private String phone;
    private Long packageId;
    private LocalDateTime expireTime;
    private Integer status;
}