package com.cn.saasplatform.entity.tenant;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_tenant_package")
public class TenantPackage extends BaseEntity {
    private String packageName;
    private String packageType;
    private String menuIds;
    private Integer sort;
    private Integer status;
}