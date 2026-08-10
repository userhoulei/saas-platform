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

    // 业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常：{}", e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * JSON请求体 @RequestBody + @Valid 校验失败抛出
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), msg);
    }

    /**
     * form表单提交 @Valid 校验失败抛出
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        String msg = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), msg);
    }

    // SpringSecurity 权限认证相关异常
    /**
     * 未登录/Token失效异常
     */
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public Result<?> handleInsufficientAuthException(InsufficientAuthenticationException e) {
        log.warn("用户未登录或身份凭证失效：{}", e.getMessage());
        return Result.fail(ResultCode.UNAUTHORIZED.getCode(), "请先登录系统");
    }

    /**
     * 已登录但无访问权限
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("用户权限不足：{}", e.getMessage());
        return Result.fail(ResultCode.FORBIDDEN.getCode(), "当前账号没有该操作权限");
    }


    // 所有其他未知异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统未知异常", e);
        return Result.fail(ResultCode.FAIL);
    }

}
