package com.gong.blog.manage.handler;


import com.alibaba.fastjson2.JSON;
import com.gong.blog.common.common.ResponseStatus;
import com.gong.blog.common.vo.AuthResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理 Spring Security 权限认证过程中出现的异常
 */
@Slf4j
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 防止中文乱码
        response.setHeader("content-type","application/json;charset=utf-8");
        AuthResult result = AuthResult.error(ResponseStatus.FORBIDDEN, request.getRequestURI() +",无权访问");
        response.getWriter().println(JSON.toJSONString(result));
    }
}
