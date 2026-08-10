package com.cn.saasplatform.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cn.saasplatform.entity.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /** 根据用户id查询权限标识集合 */
    List<String> selectPermsByUserId(Long userId);
}