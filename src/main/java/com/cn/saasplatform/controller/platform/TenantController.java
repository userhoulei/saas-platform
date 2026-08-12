package com.cn.saasplatform.controller.platform;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/platform/tenant")
@PreAuthorize("hasRole('PLATFORM_ADMIN')")
public class TenantController {
    // 租户分页、新增、编辑、冻结、续费、重置管理员账号
}
