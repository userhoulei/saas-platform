package com.cn.saasplatform.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.saasplatform.entity.system.SysRole;
import com.cn.saasplatform.entity.system.SysRoleMenu;
import com.cn.saasplatform.entity.system.SysUserRole;
import com.cn.saasplatform.mapper.system.SysRoleMapper;
import com.cn.saasplatform.mapper.system.SysRoleMenuMapper;
import com.cn.saasplatform.mapper.system.SysUserRoleMapper;
import com.cn.saasplatform.service.system.ISysRoleService;
import com.cn.saasplatform.util.TenantContextUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements ISysRoleService {

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    // ========== 新增角色并绑定菜单（事务保证原子性） ==========
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertRoleAndMenu(SysRole role, List<Long> menuIds) {
        // 1. 保存角色主表 sys_role
        save(role);
        Long roleId = role.getId();
        Long tenantId = TenantContextUtil.getTenantId();

        // 2. 批量插入 sys_role_menu 角色-菜单中间表
        List<SysRoleMenu> list = new ArrayList<>();
        for (Long menuId : menuIds) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(menuId);
            rm.setTenantId(tenantId);
            list.add(rm);
        }
        if (!list.isEmpty()) {
            sysRoleMenuMapper.batchInsert(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleAndMenu(SysRole role, List<Long> menuIds) {
        // 1. 更新角色基础信息
        updateById(role);
        Long roleId = role.getId();
        Long tenantId = TenantContextUtil.getTenantId();

        // 2. 删除该角色所有历史菜单绑定
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        sysRoleMenuMapper.delete(wrapper);

        // 3. 批量插入新的菜单绑定
        List<SysRoleMenu> list = new ArrayList<>();
        for (Long mid : menuIds) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(roleId);
            rm.setMenuId(mid);
            rm.setTenantId(tenantId);
            list.add(rm);
        }
        if (!list.isEmpty()) {
            sysRoleMenuMapper.batchInsert(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleBatch(List<Long> roleIds) {
        // 1. 删除角色主数据
        removeByIds(roleIds);

        // 2. 删除 sys_role_menu 关联
        LambdaQueryWrapper<SysRoleMenu> rmWrapper = new LambdaQueryWrapper<>();
        rmWrapper.in(SysRoleMenu::getRoleId, roleIds);
        sysRoleMenuMapper.delete(rmWrapper);
    }

    // ========== 根据角色ID查绑定的菜单ID ==========
    @Override
    public List<Long> selectMenuIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> list = sysRoleMenuMapper.selectList(wrapper);
        return list.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Override
    public void checkRoleUsed(List<Long> roleIds) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysUserRole::getRoleId, roleIds);
        Long count = sysUserRoleMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("所选角色已分配给用户使用，无法删除");
        }
    }

    @Override
    public void checkRoleDataScope(Long roleId) {
        Long currentTenant = TenantContextUtil.getTenantId();
        SysRole role = getById(roleId);
        // 租户不能修改平台角色（tenant=0），也不能修改其他租户角色
        if (!currentTenant.equals(role.getTenantId())) {
            throw new RuntimeException("无权限操作该角色数据");
        }
    }

}