package com.cn.saasplatform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j配置类
 * 用于配置Swagger接口文档的相关信息
 */
@Configuration
public class Knife4jConfig {

    /**
     * 自定义OpenAPI Bean配置
     * 用于设置API文档的基本信息，如标题、描述和版本号
     *
     * @return 返回配置好的OpenAPI实例
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("轻量化教育SaaS多租户学习中台")  // 设置API文档标题
                        .description("租户+教培行业业务")        // 设置API文档描述
                        .version("V1.0"));                     // 设置API文档版本号
    }
}