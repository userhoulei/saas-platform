package com.cn.saasplatform.config.filter;

import cn.hutool.core.util.StrUtil;
import com.cn.saasplatform.entity.system.SysUser;
import com.cn.saasplatform.entity.dto.LoginUser;
import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.entity.resp.ResultCode;
import com.cn.saasplatform.mapper.system.SysMenuMapper;
import com.cn.saasplatform.mapper.system.SysUserMapper;
import com.cn.saasplatform.util.JwtUtil;
import com.cn.saasplatform.util.TenantContextUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT过滤器
 * OncePerRequestFilter 保证一次请求只执行一次
 * 核心逻辑：
 * 1. 获取Header中的token
 * 2. 解析token，拿到用户信息、租户ID
 * 3. 存入Security上下文 + 租户上下文
 * 4. 无token/token失效，不设置认证信息，后续拦截器拦截
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.header}")
    private String headerKey;    // JWT token在请求头中的key名称
    @Value("${jwt.prefix}")
    private String tokenPrefix;  // JWT token的前缀，如"Bearer"

    @Resource
    private JwtUtil jwtUtil;     // JWT工具类，用于token的解析和验证

    @Resource
    private ObjectMapper objectMapper;  // JSON处理工具类

    @Resource
    private SysUserMapper sysUserMapper;  // 用户数据访问层

    @Resource
    private SysMenuMapper sysMenuMapper;  // 菜单/权限数据访问层

    /**
     * 过滤器核心方法
     * @param request 当前HTTP请求
     * @param response 当前HTTP响应
     * @param filterChain 过滤器链
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // 从请求头获取token
            String token = request.getHeader(headerKey);
            // 判断token格式是否正确
            if (StrUtil.isBlank(token) || !token.startsWith(tokenPrefix + " ")) {
                filterChain.doFilter(request, response);
                return;
            }
            // 去除token前缀，获取纯净token
            token = token.substring(tokenPrefix.length() + 1);
            // 解析token获取载荷
            Claims claims = jwtUtil.getClaims(token);
            if (claims == null) {
                // token无效，返回统一json错误响应
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(Result.fail(ResultCode.UNAUTHORIZED)));
                return;
            }

            // 解析载荷信息
            Long userId = Long.valueOf(claims.get("userId").toString());
            Long tenantId = Long.valueOf(claims.get("tenantId").toString());
            String username = claims.get("username").toString();

            // 查询带权限的完整LoginUser
            LoginUser loginUser = getLoginUserByUserId(userId);
            if (loginUser == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // ✅关键：自动设置租户上下文！不再依赖前端传Tenant-Id请求头
            TenantContextUtil.setTenantId(tenantId);

            // 存入Security上下文，标记已认证
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        }finally {
            TenantContextUtil.clear(); //无论成功/异常，请求结束一定清理
        }
    }

    /**
     * 根据用户ID查询完整用户信息+权限集合
     */
    private LoginUser getLoginUserByUserId(Long userId) {
        // 查询用户基础信息
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            return null;
        }
        // 查询权限标识
        List<String> permsList = sysMenuMapper.selectPermsByUserId(userId);
        // 过滤空权限，转为Security授权对象
        List<GrantedAuthority> authorities = permsList.stream()
                .filter(StrUtil::isNotBlank)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 构建完整LoginUser
        return new LoginUser(
                sysUser.getId(),
                sysUser.getTenantId(),
                sysUser.getUsername(),
                sysUser.getPassword(),
                authorities
        );
    }
}