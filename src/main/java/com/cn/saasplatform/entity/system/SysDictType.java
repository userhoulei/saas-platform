package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_dict_type")
public class SysDictType extends BaseEntity {
    private String dictName;
    private String dictType;
    private Integer status;
}