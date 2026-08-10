package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;

/**
 * 系统用户
 * 讲解：继承BaseEntity，自带id、tenantId、createTime、updateTime、isDeleted
 */
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {

    /**
     * 登录账号
     */
    private String username;

    /**
     * BCrypt加密密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 账号状态 0禁用 1正常
     */
    private Integer status;
}
