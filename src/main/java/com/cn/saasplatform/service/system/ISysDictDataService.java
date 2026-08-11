package com.cn.saasplatform.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.saasplatform.entity.system.SysDictData;

import java.util.List;

public interface ISysDictDataService extends IService<SysDictData> {
    List<SysDictData> listByDictType(String dictType);
}