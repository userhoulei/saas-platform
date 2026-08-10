package com.cn.saasplatform.util;

import cn.hutool.core.util.StrUtil;
import com.cn.saasplatform.entity.resp.ResultCode;
import com.cn.saasplatform.exception.BusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretStr;

    @Value("${jwt.expire}")
    private Long expire;

    private SecretKey secretKey;

    // AccessToken 有效期 30分钟
    private static final long ACCESS_TOKEN_EXPIRE = 1000 * 60 * 30;
    // RefreshToken 有效期 7天
    private static final long REFRESH_TOKEN_EXPIRE = 1000 * 60 * 60 * 24 * 7;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secretStr.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成token
     */
    public String generateToken(Long userId, Long tenantId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("tenantId", tenantId);
        claims.put("username", username);

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析token，获取载荷
     */
    public Claims getClaims(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // token过期
            return null;
        } catch (Exception e) {
            // token非法、篡改
            return null;
        }
    }

    /**
     * 生成访问令牌
     */
    public String createAccessToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return buildToken(claims, ACCESS_TOKEN_EXPIRE);
    }

    /**
     * 生成刷新令牌
     */
    public String createRefreshToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return buildToken(claims, REFRESH_TOKEN_EXPIRE);
    }
    /**
     * 构建JWT
     */
    private String buildToken(Map<String, Object> claims, long expireTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 从Token中获取用户ID
     */
    public Long getUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return Long.valueOf(claims.get("userId").toString());
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ResultCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.REFRESH_TOKEN_INVALID);
        }
    }

    /**
     * 校验RefreshToken是否有效
     */
    public boolean checkRefreshToken(String refreshToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            return true;
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ResultCode.REFRESH_TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.REFRESH_TOKEN_INVALID);
        }
    }
}