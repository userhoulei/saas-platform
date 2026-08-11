package com.cn.saasplatform.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.saasplatform.entity.system.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据角色id查询菜单id列表
     */
    List<Long> selectMenuIdsByRoleId(Long roleId);

    /**
     * 根据用户id查询权限菜单
     */
    List<SysMenu> selectMenusByUserId(Long userId);

    /**
     * 根据用户id查询权限标识集合
     */
    List<String> selectPermsByUserId(Long userId);
}