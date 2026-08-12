package com.cn.saasplatform.controller.platform;

import com.cn.saasplatform.service.system.ISysMenuService;
import com.cn.saasplatform.service.platform.ITenantPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/platform/package")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class TenantPackageController {

    @Autowired
    private ITenantPackageService tenantPackageService;

    @Autowired
    private ISysMenuService menuService;

}