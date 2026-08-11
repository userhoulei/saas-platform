package com.cn.saasplatform.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cn.saasplatform.entity.system.SysOperLog;
import com.cn.saasplatform.mapper.system.SysOperLogMapper;
import com.cn.saasplatform.service.system.ISysOperLogService;
import org.springframework.stereotype.Service;

@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements ISysOperLogService {
}
