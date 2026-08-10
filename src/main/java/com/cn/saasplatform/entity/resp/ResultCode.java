package com.cn.saasplatform.entity.resp;
/**
 * 全局响应码
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),

    // 客户端 4xx
    PARAM_ERROR(400, "参数非法"),
    UNAUTHORIZED(401, "未登录或token失效"),
    FORBIDDEN(403, "权限不足"),
    NOT_FOUND(404, "资源不存在"),

    // 业务自定义 6xx
    USER_NOT_EXIST(6001, "用户不存在"),
    USERNAME_PASSWORD_ERROR(6002, "账号或密码错误"),
    // JWT令牌异常
    TOKEN_EXPIRED(6003, "登录令牌已过期，请重新登录"),
    REFRESH_TOKEN_INVALID(6004, "刷新令牌无效"),
    REFRESH_TOKEN_EXPIRED(6005, "刷新令牌已失效，请重新登录"),
    // 切面异常
    REPEAT_SUBMIT_ERROR(6006, "请勿重复提交请求"),
    RATE_LIMIT_ERROR(6007, "访问过于频繁，请稍后再试");


    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}