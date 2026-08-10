package com.cn.saasplatform.entity.dto;


import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class SysUserDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String nickname;
    @NotBlank
    private String password;
    private Integer status;
}