package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.base.BaseEntity;
import lombok.Data;

/**
 * 系统用户角色关联实体类
 * 用于存储用户与角色之间的关联关系
 * 使用@Data注解自动生成getter、setter等方法
 * 使用@TableName注解指定对应的数据库表名为"sys_user_role"
 */
@Data
@TableName("sys_user_role")
public class SysUserRole extends BaseEntity {
    /**
     * 用户ID
     * 用于标识系统中的用户
     */
    private Long userId;
    /**
     * 角色ID
     * 用于标识系统中的角色
     */
    private Long roleId;
}