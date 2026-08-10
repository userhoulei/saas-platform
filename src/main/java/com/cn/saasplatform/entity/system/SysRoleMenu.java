package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_role_menu")
public class SysRoleMenu extends BaseEntity {
    private Long roleId;
    private Long menuId;
}