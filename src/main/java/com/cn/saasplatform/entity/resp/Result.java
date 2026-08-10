package com.cn.saasplatform.entity.resp;

import lombok.Data;

/**
 * 接口统一返回封装
 * 所有Controller禁止直接返回原始对象，统一包装Result
 * 前端固定解析格式：code、msg、data，前后端协作标准
 * 泛型<T>兼容任意返回数据（分页、实体、列表）
 */
@Data
public class Result<T> {

    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return build(ResultCode.SUCCESS, null);
    }

    public static <T> Result<T> success(T data) {
        return build(ResultCode.SUCCESS, data);
    }

    public static <T> Result<T> fail(ResultCode code) {
        Result<T> r = new Result<>();
        r.setCode(code.getCode());
        r.setMsg(code.getMsg());
        return r;
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    private static <T> Result<T> build(ResultCode rc, T data) {
        Result<T> r = new Result<>();
        r.setCode(rc.getCode());
        r.setMsg(rc.getMsg());
        r.setData(data);
        return r;
    }
}