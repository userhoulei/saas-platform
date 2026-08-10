package com.cn.saasplatform.controller;

import com.cn.saasplatform.entity.system.SysUser;
import com.cn.saasplatform.entity.dto.LoginDTO;
import com.cn.saasplatform.entity.dto.LoginUser;
import com.cn.saasplatform.entity.dto.RefreshTokenDTO;
import com.cn.saasplatform.entity.resp.Result;
import com.cn.saasplatform.entity.resp.ResultCode;
import com.cn.saasplatform.entity.vo.TokenVO;
import com.cn.saasplatform.exception.BusinessException;
import com.cn.saasplatform.mapper.system.SysUserMapper;
import com.cn.saasplatform.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private SysUserMapper sysUserMapper;

    @PostMapping("/login")
    public Result<Map<String,String>> login(@Valid @RequestBody LoginDTO dto) {
        // 认证账号密码
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        String token = jwtUtil.generateToken(loginUser.getUserId(), loginUser.getTenantId(), loginUser.getUsername());
        Map<String,String> data = new HashMap<>();
        data.put("token", token);
        return Result.success(data);
    }

    @PostMapping("/refreshToken")
    public Result<Map<String, String>> refreshToken(@Valid @RequestBody RefreshTokenDTO dto) {
        String refreshToken = dto.getRefreshToken();
        // 校验刷新令牌
        jwtUtil.checkRefreshToken(refreshToken);
        Long userId = jwtUtil.getUserId(refreshToken);
        // 生成新令牌对
        String newAccess = jwtUtil.createAccessToken(userId);
        String newRefresh = jwtUtil.createRefreshToken(userId);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", newAccess);
        tokenMap.put("refreshToken", newRefresh);
        return Result.success(tokenMap);
    }
}