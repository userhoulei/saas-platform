package com.cn.saasplatform.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.saasplatform.entity.system.SysMenu;
import com.cn.saasplatform.entity.system.SysRole;
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

    /**
     * 获取角色列表接口
     *
     * @param pageNum  页码，默认为1
     * @param pageSize 每页大小，默认为10
     * @param roleName 角色名称（可选），用于模糊查询
     * @param status   状态（可选），用于精确匹配
     * @return 返回分页后的角色数据
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:role:list')")  // 权限校验，需要拥有system:role:list权限
    public Result<Page<SysRole>> list(
            @RequestParam(defaultValue = "1") Long pageNum,    // 页码参数，默认值为1
            @RequestParam(defaultValue = "10") Long pageSize,    // 每页大小参数，默认值为10
            @RequestParam(required = false) String roleName,    // 角色名称参数，非必需
            @RequestParam(required = false) Integer status       // 状态参数，非必需
    ) {
        // 创建分页对象
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        // 创建Lambda查询条件构造器
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(roleName != null && !roleName.isEmpty(), SysRole::getRoleName, roleName);
        queryWrapper.eq(status != null, SysRole::getStatus, status);

        Page<SysRole> res = sysRoleService.page(page, queryWrapper);
        return Result.success(res);
    }

    /**
     * 根据角色ID查询角色信息
     *
     * @param roleId 角色ID，通过路径变量传递
     * @return 返回查询到的角色信息，封装在Result对象中
     */
    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<SysRole> getInfo(@PathVariable Long roleId) {
        SysRole role = sysRoleService.getById(roleId);
        return Result.success(role);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('system:role:add')")
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
    @PreAuthorize("hasAuthority('system:role:edit')")
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
    @PreAuthorize("hasAuthority('system:role:remove')")
    public Result<Void> remove(@PathVariable List<Long> roleIds) {
        // 1. 校验是否被用户绑定（如果角色已分配给用户，禁止删除）
        sysRoleService.checkRoleUsed(roleIds);
        // 2. 删除角色本身 + 删除该角色所有菜单关联记录
        sysRoleService.deleteRoleBatch(roleIds);
        return Result.success();
    }

    /**
     * 根据角色 ID 回显已绑定菜单 ID
     *
     * @param roleId 角色ID，通过路径变量传递
     * @return 菜单ID列表
     */
    @GetMapping("/role/{roleId}")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long roleId) {
        return Result.success(sysRoleService.selectMenuIdsByRoleId(roleId));
    }

    /**
     * 获取菜单树结构接口
     * 该接口用于获取系统菜单的树形结构数据
     * 需要用户拥有'system:role:list'权限才能访问
     *
     * @return 菜单树结构数据
     */
    @GetMapping("/menuTree")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<List<SysMenu>> getMenuTree() {
        List<SysMenu> tree = sysMenuService.buildRoleMenuTree();
        return Result.success(tree);
    }
}