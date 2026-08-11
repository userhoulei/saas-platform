package com.cn.saasplatform.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cn.saasplatform.entity.BaseEntity;
import lombok.Data;

/**
 * 系统操作日志实体类
 * 用于记录系统操作的相关信息，包括操作人、操作模块、操作描述等
 */
@Data
@TableName("sys_oper_log")
public class SysOperLog extends BaseEntity {
    /**
     * 用户ID
     * 记录执行操作的用户ID
     */
    private Long userId;
    /**
     * 用户名
     * 记录执行操作的用户名
     */
    private String username;
    /**
     * 操作模块
     * 记录用户操作的功能模块
     */
    private String operModule;
    /**
     * 操作描述
     * 记录用户操作的具体描述信息
     */
    private String operDesc;
    /**
     * 请求URL
     * 记录用户操作的请求地址
     */
    private String requestUrl;
    /**
     * 请求方法
     * 记录用户操作的请求方法，如GET、POST等
     */
    private String requestMethod;
    /**
     * IP地址
     * 记录用户操作时的客户端IP地址
     */
    private String ipAddress;
    /**
     * 请求参数
     * 记录用户操作的请求参数信息
     */
    private String requestParams;
    /**
     * 耗时
     * 记录用户操作所花费的时间，单位为毫秒
     */
    private Long costTime;
    /**
     * 操作状态
     * 记录用户操作的状态，如成功、失败等
     */
    private Integer status;
    /**
     * 错误信息
     * 记录操作失败时的错误信息
     */
    private String errorMsg;
}