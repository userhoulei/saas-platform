package com.cn.saasplatform.controller.system;

import com.cn.saasplatform.aspect.annotation.OperateLog;
import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.entity.system.SysMenu;
import com.cn.saasplatform.service.system.ISysMenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class SysMenuController {

    @Resource
    private ISysMenuService sysMenuService;

    /**
     * 获取菜单树结构的接口
     * 该接口需要用户拥有'system:menu:list'权限才能访问
     *
     * @return 返回一个包含菜单树结构的Result对象，其中包含菜单列表数据
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:menu:list')") // 只有拥有指定权限的用户才能访问此接口
    public Result<List<SysMenu>> tree() {
        return Result.success(sysMenuService.getMenuTree());
    }

    @GetMapping("/role/{roleId}")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long roleId) {
        return Result.success(sysMenuService.getMenuIdsByRoleId(roleId));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('system:menu:add')")
    @OperateLog("新增菜单")
    public Result<Void> add(@RequestBody SysMenu menu) {
        sysMenuService.save(menu);
        return Result.success();
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    @OperateLog("修改菜单")
    public Result<Void> update(@RequestBody SysMenu menu) {
        sysMenuService.updateById(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:remove')")
    @OperateLog("删除菜单")
    public Result<Void> delete(@PathVariable Long id) {
        sysMenuService.deleteMenuById(id);
        return Result.success();
    }
}