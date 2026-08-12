package com.cn.saasplatform.entity.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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

    /**
     * 租户ID：新增自动填充，更新不修改
     */
    @TableField(fill = FieldFill.INSERT)
    private Long tenantId;

    /**
     * 创建时间：仅插入填充
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间：插入+更新都填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}