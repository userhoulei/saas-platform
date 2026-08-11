package com.cn.saasplatform.config.securityhandler;

import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.entity.resp.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 已登录但是没有访问权限
 * 用于处理用户已登录但没有访问权限的情况
 */
@Component
public class ForbiddenHandler implements AccessDeniedHandler { // 实现AccessDeniedHandler接口，用于处理访问被拒绝的情况

    private final ObjectMapper objectMapper = new ObjectMapper(); // 创建ObjectMapper实例，用于将对象转换为JSON格式

    /**
     * 处理访问被拒绝的方法
     * @param request 当前HTTP请求
     * @param response 当前HTTP响应
     * @param accessDeniedException 访问被拒绝异常
     * @throws IOException 可能抛出的IO异常
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 设置响应内容类型为JSON，字符编码为UTF-8
        response.setContentType("application/json;charset=utf-8");
        // 将Result.fail(ResultCode.FORBIDDEN)对象转换为JSON字符串并写入响应输出流
        response.getWriter().write(objectMapper.writeValueAsString(Result.fail(ResultCode.FORBIDDEN)));
    }
}