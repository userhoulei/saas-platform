package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;

/**
 * 系统角色菜单关联实体类
 * 用于存储角色与菜单的关联关系
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenu extends BaseEntity {
    /**
     * 角色ID
     * 关联sys_role表的主键
     */
    private Long roleId;
    private Long menuId;
}