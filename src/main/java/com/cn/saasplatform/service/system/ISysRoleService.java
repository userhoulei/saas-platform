package com.cn.saasplatform.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.saasplatform.entity.system.SysRole;
import java.util.List;

public interface ISysRoleService extends IService<SysRole> {

    // 分页查询角色
//    PageResult<SysRole> selectRolePage(Long pageNum, Long pageSize, String roleName, Integer status);

    // 新增角色 + 批量绑定菜单
    void insertRoleAndMenu(SysRole role, List<Long> menuIds);

    // 修改角色 + 重新绑定菜单（先删旧关联，再加新关联）
    void updateRoleAndMenu(SysRole role, List<Long> menuIds);

    // 批量删除角色，级联删除sys_role_menu
    void deleteRoleBatch(List<Long> roleIds);

    // 根据角色ID查询绑定的所有菜单ID
    List<Long> selectMenuIdsByRoleId(Long roleId);

    // 校验角色是否被用户引用（被sys_user_role关联则不能删）
    void checkRoleUsed(List<Long> roleIds);

    // 数据权限校验：只能操作自己租户下的角色
    void checkRoleDataScope(Long roleId);
}