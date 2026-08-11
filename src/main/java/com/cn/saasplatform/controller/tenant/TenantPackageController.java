package com.cn.saasplatform.controller.tenant;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/platform/package")
@PreAuthorize("hasRole('PLATFORM_ADMIN')")
public class TenantPackageController {
    // 套餐CRUD、选择菜单树保存menuIds字符串
}