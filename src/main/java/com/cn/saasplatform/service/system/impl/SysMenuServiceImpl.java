package com.cn.saasplatform.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.saasplatform.entity.system.SysMenu;
import com.cn.saasplatform.mapper.system.SysMenuMapper;
import com.cn.saasplatform.service.system.ISysMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
        implements ISysMenuService {

    @Override
    public void addMenu(SysMenu sysMenu) {

    }

    @Override
    public void updateMenu(SysMenu sysMenu) {

    }

    @Override
    public SysMenu getMenuInfoById(Long menuId) {
        return null;
    }

    @Override
    public boolean hasChildMenu(Long menuId) {
        return false;
    }

    /**
     * 获取菜单树形结构列表
     * 重写父类方法，返回所有状态为正常的菜单树
     *
     * @return 菜单树形结构列表
     */
    @Override
    public List<SysMenu> getMenuTree() {
        // 查询所有状态为1（正常）的菜单列表
        List<SysMenu> allList = list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));
        // 调用buildTree方法构建树形结构，0L表示顶级菜单的父ID
        return buildTree(allList, 0L);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        return baseMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    public List<SysMenu> getUserMenuTree(Long userId) {
        List<SysMenu> menus = baseMapper.selectMenusByUserId(userId);
        return buildTree(menus, 0L);
    }

    @Override
    public void deleteMenuById(Long menuId) {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, menuId);
        long count = count(wrapper);
        if (count > 0) {
            throw new RuntimeException("存在子菜单，无法删除");
        }
        removeById(menuId);
    }

    @Override
    public List<SysMenu> buildRoleMenuTree(Long tenantId) {
        return List.of();
    }

    @Override
    public boolean menuIsBindRole(Long menuId) {
        return false;
    }

    @Override
    public List<Long> getAllChildMenuIds(Long parentId) {
        return List.of();
    }

    /**
     * 递归构建树形结构
     * 该方法通过递归方式将扁平化的菜单列表转换为树形结构
     *
     * @param list     扁平化的菜单列表
     * @param parentId 父菜单ID，用于递归查找子节点
     * @return 构建好的树形结构菜单列表
     */
    private List<SysMenu> buildTree(List<SysMenu> list, Long parentId) {
        List<SysMenu> tree = new ArrayList<>(); // 用于存储当前层级的菜单节点
        // 遍历菜单列表，查找属于当前父节点的子菜单
        for (SysMenu menu : list) {
            // 如果菜单的父ID等于传入的父ID，则该菜单是子节点
            if (menu.getParentId().equals(parentId)) {
                // 递归查找该菜单的子节点，并设置为其children属性
                menu.setChildren(buildTree(list, menu.getId()));
                // 将构建好的菜单节点添加到树中
                tree.add(menu);
            }
        }
        // 对当前层级的菜单按排序字段进行升序排序后返回
        return tree.stream().sorted((a, b) -> a.getSort() - b.getSort()).collect(Collectors.toList());
    }
}