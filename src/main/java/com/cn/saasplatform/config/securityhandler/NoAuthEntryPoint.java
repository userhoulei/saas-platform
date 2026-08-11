package com.cn.saasplatform.config.securityhandler;

import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.entity.resp.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登录/token失效处理
 * 实现AuthenticationEntryPoint接口，用于处理认证入口点
 * 当用户未登录或token失效时，会调用此类的commence方法
 */
@Component
public class NoAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * 使用ObjectMapper将对象转换为JSON字符串
     * 用于将Result对象转换为JSON格式返回给前端
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 处理未认证请求的方法
     * @param request HttpServletRequest对象，包含请求信息
     * @param response HttpServletResponse对象，用于返回响应
     * @param authException AuthenticationException对象，包含认证异常信息
     * @throws IOException 可能抛出的IO异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 设置响应内容类型为JSON，字符编码为UTF-8
        response.setContentType("application/json;charset=utf-8");
        // 将Result对象转换为JSON字符串并写入响应输出流
        response.getWriter().write(objectMapper.writeValueAsString(Result.fail(ResultCode.UNAUTHORIZED)));
    }
}