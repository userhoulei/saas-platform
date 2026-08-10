package com.cn.saasplatform.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cn.saasplatform.entity.system.SysUser;
import com.cn.saasplatform.entity.dto.LoginUser;
import com.cn.saasplatform.entity.resp.ResultCode;
import com.cn.saasplatform.exception.BusinessException;
import com.cn.saasplatform.mapper.system.SysMenuMapper;
import com.cn.saasplatform.mapper.system.SysUserMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (sysUser == null) {
            throw new BusinessException(ResultCode.USERNAME_PASSWORD_ERROR);
        }
        // 判断账号是否禁用
        if (sysUser.getStatus() == 0) {
            throw new BusinessException(500,"账号已被禁用");
        }

        // 查询用户权限标识
        List<String> permsList = sysMenuMapper.selectPermsByUserId(sysUser.getId());
        List<GrantedAuthority> authorities = permsList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new LoginUser(sysUser.getId(), sysUser.getTenantId(),
                sysUser.getUsername(), sysUser.getPassword(), authorities);
    }
}