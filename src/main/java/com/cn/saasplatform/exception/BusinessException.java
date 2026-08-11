package com.cn.saasplatform.exception;

import com.cn.saasplatform.entity.resp.ResultCode;
import lombok.Data;

/**
 * 业务异常
 * 继承自RuntimeException，用于处理业务逻辑中的异常情况
 * 使用@Data注解自动生成getter、setter等方法
 */
@Data
public class BusinessException extends RuntimeException {

    // 异常代码，使用final修饰确保不可变性
    private final Integer code;

    /**
     * 构造方法1：使用ResultCode枚举创建异常
     * @param resultCode 包含错误代码和错误信息的枚举
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    /**
     * 构造方法2：自定义错误代码和错误信息
     * @param code 自定义错误代码
     * @param message 自定义错误信息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
