package com.cn.saasplatform.service.platform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.saasplatform.entity.platform.TenantPackage;

import java.util.List;

public interface ITenantPackageService extends IService<TenantPackage> {
    List<Long> getTenantAllowMenuIdList(Long tenantId);
}
