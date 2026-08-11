package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;
import java.util.List;

/**
 * 系统菜单实体类
 * 继承自BaseEntity，包含菜单的基本属性和子菜单列表
 */
@Data
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    private Long parentId;    // 父菜单ID，用于构建菜单树结构

    private String menuName;  // 菜单名称

    private String menuType;  // 菜单类型（目录、菜单、按钮）

    private String path;      // 路由地址

    private String component; // 前端组件路径

    private String perms;     // 权限标识

    private String icon;      // 菜单图标

    private Integer sort;     // 显示顺序

    private Integer status;   // 菜单状态（0显示 1隐藏）

    /**
     * 子菜单列表
     * 不在数据库表中存在，仅用于构建树形结构
     */
    @TableField(exist = false)
    private List<SysMenu> children;
}