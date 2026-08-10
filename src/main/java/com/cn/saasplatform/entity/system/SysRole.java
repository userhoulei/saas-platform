package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {
    private String roleName;
    private String roleCode;
    private String remark;
    private Integer status;
}