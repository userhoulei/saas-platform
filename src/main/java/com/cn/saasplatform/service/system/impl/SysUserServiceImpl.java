package com.cn.saasplatform.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.saasplatform.entity.system.SysUser;
import com.cn.saasplatform.entity.dto.SysUserDTO;
import com.cn.saasplatform.mapper.system.SysUserMapper;
import com.cn.saasplatform.service.system.ISysUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public void addUser(SysUserDTO dto) {
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setStatus(dto.getStatus());
        // 密码加密
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        save(user);
    }
}