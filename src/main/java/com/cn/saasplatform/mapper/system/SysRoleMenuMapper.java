package com.cn.saasplatform.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.saasplatform.entity.system.SysRoleMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 批量插入角色-菜单关联数据
     * @param list 关联集合
     */
    void batchInsert(@Param("list") List<SysRoleMenu> list);
}