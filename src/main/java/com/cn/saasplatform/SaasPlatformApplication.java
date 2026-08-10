package com.cn.saasplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.cn.saasplatform.mapper") // 扫描Mapper
@EnableAsync // 开启异步
@EnableScheduling // 开启定时任务
public class SaasPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaasPlatformApplication.class, args);
    }

}
