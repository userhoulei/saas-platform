package com.cn.saasplatform.util;

import com.cn.saasplatform.entity.system.SysDictData;
import com.cn.saasplatform.service.system.ISysDictDataService;
import javax.annotation.Resource;
import java.util.List;

public class DictUtil {

    private static ISysDictDataService dictDataService;

    @Resource
    public void setDictDataService(ISysDictDataService service) {
        dictDataService = service;
    }

    /**
     * 根据字典类型编码获取下拉列表
     */
    public static List<SysDictData> getDictList(String dictType) {
        return dictDataService.listByDictType(dictType);
    }
}