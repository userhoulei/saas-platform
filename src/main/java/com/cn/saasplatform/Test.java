package com.cn.saasplatform;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Test {


    public static void main(String[] args){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        //填入你的原始明文密码
        String pwd = "123456";
        String encrypt = encoder.encode(pwd);
        System.out.println(encrypt);
    }
}
