package com.cn.saasplatform.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.saasplatform.entity.system.SysUser;
import com.cn.saasplatform.entity.dto.SysUserDTO;
import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.service.system.SysUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/system/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @PreAuthorize("hasAuthority('system:user:list')")
    @GetMapping("/page")
    public Result<Page<SysUser>> page(@RequestParam(defaultValue = "1") Long pageNum,
                                      @RequestParam(defaultValue = "10") Long pageSize){
        Page<SysUser> page = new Page<>(pageNum,pageSize);
        Page<SysUser> res = sysUserService.page(page,new LambdaQueryWrapper<>());
        return Result.success(res);
    }

    @PreAuthorize("hasAuthority('system:user:add')")
    @PostMapping("/add")
    public Result<Void> add(@Valid @RequestBody SysUserDTO dto){
        sysUserService.addUser(dto);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('system:user:remove')")
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Long id){
        sysUserService.removeById(id);
        return Result.success();
    }
}
