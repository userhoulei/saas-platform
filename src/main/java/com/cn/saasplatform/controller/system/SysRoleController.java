package com.cn.saasplatform.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.saasplatform.entity.system.SysMenu;
import com.cn.saasplatform.entity.system.SysRole;
import com.cn.saasplatform.entity.system.SysUser;
import com.cn.saasplatform.service.system.ISysMenuService;
import com.cn.saasplatform.service.system.ISysRoleService;
import com.cn.saasplatform.util.TenantContextUtil;
import com.cn.saasplatform.entity.resp.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 系统角色控制器
 * RBAC核心：角色维护 + 角色绑定菜单权限
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    @Resource
    private ISysRoleService sysRoleService;

    @Resource
    private ISysMenuService sysMenuService;

/*    @GetMapping("/list")
    @PreAuthorize("hasPermission('system:role:list')")
    public Result<PageResult<SysRole>> list(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Integer status
    ) {
        PageResult<SysRole> page = sysRoleService.selectRolePage(pageNum, pageSize, roleName, status);
        return Result.success(page);
    }*/

    @GetMapping("/{roleId}")
    @PreAuthorize("hasPermission('system:role:query')")
    public Result<SysRole> getInfo(@PathVariable Long roleId) {
        SysRole role = sysRoleService.getById(roleId);
        return Result.success(role);
    }

    @PostMapping("/add")
    @PreAuthorize("hasPermission('system:role:add')")
    public Result<Void> add(
            @Valid @RequestBody SysRole sysRole,
            @RequestParam List<Long> menuIds
    ) {
        // 自动填充tenantId（平台管理员=0，租户=当前上下文租户ID）
        Long tenantId = TenantContextUtil.getTenantId();
        sysRole.setTenantId(tenantId);

        // 业务层：保存角色 + 批量插入sys_role_menu关联数据
        sysRoleService.insertRoleAndMenu(sysRole, menuIds);
        return Result.success();
    }

    @PutMapping("/edit")
    @PreAuthorize("hasPermission('system:role:edit')")
    public Result<Void> edit(
            @Valid @RequestBody SysRole sysRole,
            @RequestParam List<Long> menuIds
    ) {
        // 校验数据归属：不能修改其他租户/平台角色（防止越权）
        sysRoleService.checkRoleDataScope(sysRole.getId());
        sysRoleService.updateRoleAndMenu(sysRole, menuIds);
        return Result.success();
    }

    @DeleteMapping("/remove/{roleIds}")
    @PreAuthorize("hasPermission('system:role:remove')")
    public Result<Void> remove(@PathVariable List<Long> roleIds) {
        // 1. 校验是否被用户绑定（如果角色已分配给用户，禁止删除）
        sysRoleService.checkRoleUsed(roleIds);
        // 2. 删除角色本身 + 删除该角色所有菜单关联记录
        sysRoleService.deleteRoleBatch(roleIds);
        return Result.success();
    }

    @GetMapping("/menuIds/{roleId}")
    @PreAuthorize("hasPermission('system:role:query')")
    public Result<List<Long>> getRoleBindMenuIds(@PathVariable Long roleId) {
        List<Long> menuIdList = sysRoleService.selectMenuIdsByRoleId(roleId);
        return Result.success(menuIdList);
    }

    @GetMapping("/menuTree")
    @PreAuthorize("hasPermission('system:role:query')")
    public Result<List<SysMenu>> getRoleMenuTree() {
        Long tenantId = TenantContextUtil.getTenantId();
        List<SysMenu> tree = sysMenuService.buildRoleMenuTree(tenantId);
        return Result.success(tree);
    }
}