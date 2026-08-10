package com.cn.saasplatform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据库实体父类
 * 统一封装公共字段：主键、租户ID、创建时间、更新时间、逻辑删除
 * 所有表实体继承，减少重复代码
 */
@Data
public class BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long tenantId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}