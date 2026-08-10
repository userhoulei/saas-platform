package com.cn.saasplatform.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cn.saasplatform.entity.system.SysOperLog;
import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.service.system.SysOperLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/operlog")
public class SysOperLogController {

    @Resource
    private SysOperLogService operLogService;

    @PreAuthorize("hasAuthority('system:log:list')")
    @GetMapping("/page")
    public Result<Page<SysOperLog>> page(
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String operDesc
    ) {
        Page<SysOperLog> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysOperLog> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StringUtils.hasText(username), SysOperLog::getUsername, username);
        wrapper.like(StringUtils.hasText(operDesc), SysOperLog::getOperDesc, operDesc);
        Page<SysOperLog> result = operLogService.page(page, wrapper);
        return Result.success(result);
    }
}
