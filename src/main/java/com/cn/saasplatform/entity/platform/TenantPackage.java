package com.cn.saasplatform.entity.platform;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.base.BaseEntity;
import lombok.Data;

/**
 * 租户套餐实体类
 * 继承自BaseEntity，包含套餐的基本信息
 */
@Data
@TableName("sys_tenant_package")
public class TenantPackage extends BaseEntity {
    /**
     * 套餐名称
     */
    private String packageName;
    /**
     * 套餐类型
     */
    private String packageType;
    /**
     * 关联的菜单ID集合，通常使用逗号分隔的字符串形式存储
     */
    private String menuIds;
    /**
     * 排序序号，用于控制套餐的显示顺序
     */
    private Integer sort;
    /**
     * 套餐状态，例如：0-禁用，1-启用
     */
    private Integer status;
}