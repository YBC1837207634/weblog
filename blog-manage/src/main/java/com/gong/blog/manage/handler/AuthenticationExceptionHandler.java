package com.gong.blog.manage.handler;

import com.alibaba.fastjson2.JSON;
import com.gong.blog.common.constants.ResponseStatus;
import com.gong.blog.common.vo.AuthResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理 Spring Security 认证过程中出现的异常
 */
@Slf4j
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 防止中文乱码
        response.setHeader("content-type","application/json;charset=utf-8");
        AuthResult result = AuthResult.error(ResponseStatus.INTERNAL, authException.getMessage());
        response.getWriter().println(JSON.toJSONString(result));
        log.warn(authException.getMessage());
    }
}
