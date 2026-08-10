package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("sys_oper_log")
public class SysOperLog extends BaseEntity {
    private Long userId;
    private String username;
    private String operModule;
    private String operDesc;
    private String requestUrl;
    private String requestMethod;
    private String ipAddress;
    private String requestParams;
    private Long costTime;
    private Integer status;
    private String errorMsg;
}