package com.cn.saasplatform.service.platform.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.saasplatform.entity.platform.TenantPackage;
import com.cn.saasplatform.mapper.tenant.TenantPackageMapper;
import com.cn.saasplatform.service.platform.ITenantPackageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantPackageServiceImpl extends ServiceImpl<TenantPackageMapper, TenantPackage> implements ITenantPackageService {
    @Override
    public List<Long> getTenantAllowMenuIdList(Long tenantId) {
        return List.of();
    }
}
