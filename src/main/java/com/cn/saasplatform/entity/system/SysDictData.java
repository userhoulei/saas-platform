package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_dict_data")
public class SysDictData extends BaseEntity {
    private Long dictTypeId;
    private String dictLabel;
    private String dictValue;
    private Integer sort;
    private Integer status;
}