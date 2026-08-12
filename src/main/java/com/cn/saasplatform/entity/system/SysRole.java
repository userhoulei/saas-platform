package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.base.BaseEntity;
import lombok.Data;

/**
 * 系统角色实体类
 * 继承自BaseEntity，包含角色基本信息
 * 使用@Data注解自动生成getter、setter等方法
 * @TableName("sys_role")指定对应数据库表名为sys_role
 */
@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {
    // 角色名称
    private String roleName;
    // 角色编码
    private String roleCode;
    // 备注信息
    private String remark;
    // 角色状态（例如：1-启用，0-禁用）
    private Integer status;
}