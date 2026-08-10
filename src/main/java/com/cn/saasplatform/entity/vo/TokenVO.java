package com.cn.saasplatform.entity.vo;

import lombok.Data;

@Data
public class TokenVO {
    private String accessToken;
    private String refreshToken;
}