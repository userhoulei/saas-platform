package com.cn.saasplatform.exception;

import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.entity.resp.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截
 * 作用：统一异常输出格式，避免前端收到500原始堆栈页面
 * 区分：自定义业务异常、参数校验异常、未知系统异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 业务异常处理方法
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());  // 记录业务异常日志
        return Result.fail(e.getCode(), e.getMessage());  // 返回业务异常信息
    }

    /**
     * JSON请求体 @RequestBody + @Valid 校验失败抛出异常的处理方法
     * 处理HTTP请求体参数验证失败的情况
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();  // 获取字段验证错误信息
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), msg);  // 返回参数错误信息
    }

    /**
     * form表单提交 @Valid 校验失败抛出异常的处理方法
     * 处理表单参数验证失败的情况
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();  // 获取字段验证错误信息
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), msg);  // 返回参数错误信息
    }

    // SpringSecurity 权限认证相关异常处理方法
    /**
     * 未登录/Token失效异常的处理方法
     * 当用户未登录或身份凭证失效时触发
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public Result<?> handleInsufficientAuthException(InsufficientAuthenticationException e) {
        log.warn("用户未登录或身份凭证失效：{}", e.getMessage());  // 记录警告日志
        return Result.fail(ResultCode.UNAUTHORIZED.getCode(), "请先登录系统");  // 返回未登录提示
    }

    /**
     * 已登录但无访问权限异常的处理方法
     * 当用户已登录但没有访问特定资源的权限时触发
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("用户权限不足：{}", e.getMessage());  // 记录警告日志
        return Result.fail(ResultCode.FORBIDDEN.getCode(), "当前账号没有该操作权限");  // 返回权限不足提示
    }


    // 所有其他未知异常的处理方法
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统未知异常", e);  // 记录系统异常日志，包括堆栈信息
        return Result.fail(ResultCode.FAIL);  // 返回系统错误信息
    }

}
