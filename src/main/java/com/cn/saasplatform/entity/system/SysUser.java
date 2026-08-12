package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.base.BaseEntity;
import lombok.Data;

/**
 * 系统用户实体类
 * 讲解：继承BaseEntity，自带id、tenantId、createTime、updateTime、isDeleted
 * 使用@Data注解自动生成getter、setter等方法
 * 使用@TableName注解指定对应的数据库表名为"sys_user"
 */
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {

    /**
     * 登录账号
     * 用于用户登录系统的唯一标识
     */
    private String username;

    /**
     * BCrypt加密密码
     * 存储的是经过BCrypt加密后的密码字符串
     */
    private String password;

    /**
     * 用户昵称
     * 用户在系统中显示的名称
     */
    private String nickname;

    /**
     * 账号状态 0禁用 1正常
     */
    private Integer status;
}
