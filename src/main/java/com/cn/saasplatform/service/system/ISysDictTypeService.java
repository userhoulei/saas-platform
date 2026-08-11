package com.cn.saasplatform.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.saasplatform.entity.system.SysDictType;

public interface ISysDictTypeService extends IService<SysDictType> {
    void deleteType(Long id);
}