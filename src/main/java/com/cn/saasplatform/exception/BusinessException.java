package com.cn.saasplatform.exception;

import com.cn.saasplatform.entity.resp.ResultCode;
import lombok.Data;

/**
 * 业务异常
 */
@Data
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
