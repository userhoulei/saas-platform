package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_user_role")
public class SysUserRole extends BaseEntity {
    private Long userId;
    private Long roleId;
}