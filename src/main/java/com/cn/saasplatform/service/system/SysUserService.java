package com.cn.saasplatform.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cn.saasplatform.entity.system.SysUser;
import com.cn.saasplatform.entity.dto.SysUserDTO;

public interface SysUserService extends IService<SysUser> {
    void addUser(SysUserDTO dto);
}