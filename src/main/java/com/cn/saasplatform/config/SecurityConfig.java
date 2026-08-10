package com.cn.saasplatform.config;

import com.cn.saasplatform.config.securityhandler.ForbiddenHandler;
import com.cn.saasplatform.config.securityhandler.NoAuthEntryPoint;
import com.cn.saasplatform.config.filter.JwtAuthenticationFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
// 开启注解权限 @PreAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(prefix = "app", name = "security-enabled", havingValue = "true", matchIfMissing = true)
public class SecurityConfig {

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Resource
    private NoAuthEntryPoint noAuthEntryPoint;
    @Resource
    private ForbiddenHandler forbiddenHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                // 无状态，不创建session【前后端分离JWT核心】
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // ✅ 关键新增：禁用内置表单登录，不再弹出登录页面
                .formLogin().disable()
                // 路由放行配置
                .authorizeRequests()
                // 本地调试test接口、登录、文档）
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/doc.html","/swagger-resources/**","/v3/api-docs/**").permitAll()
                //其他接口必须认证
                .anyRequest().authenticated()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(noAuthEntryPoint)
                .accessDeniedHandler(forbiddenHandler);

        // jwt过滤器放在账号密码过滤器前面
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}