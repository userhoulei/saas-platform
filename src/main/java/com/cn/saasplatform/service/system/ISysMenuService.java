package com.cn.saasplatform.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.saasplatform.entity.system.SysMenu;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {

    /**
     * 新增平台级菜单，自动填充tenantId=0
     */
    void addMenu(SysMenu sysMenu);

    /**
     * 编辑菜单名称、路由、权限标识、排序、状态等
     */
    void updateMenu(SysMenu sysMenu);

    SysMenu getMenuInfoById(Long menuId);

    /**
     * 判断当前菜单是否存在下级子菜单
     */
    boolean hasChildMenu(Long menuId);

    List<SysMenu> getMenuTree();

    List<Long> getMenuIdsByRoleId(Long roleId);

    List<SysMenu> getUserMenuTree(Long userId);

    void deleteMenuById(Long menuId);

    List<SysMenu> buildRoleMenuTree();

    /**
     * 查询该菜单ID是否被任意角色关联绑定
     */
    boolean menuIsBindRole(Long menuId);

    /**
     * 递归获取当前菜单下所有子菜单ID集合，批量删除用
     */
    List<Long> getAllChildMenuIds(Long parentId);
}