package com.gong.blog.manage.handler;

import com.alibaba.fastjson2.JSON;
import com.gong.blog.common.common.ResponseStatus;
import com.gong.blog.common.exception.TokenException;
import com.gong.blog.common.vo.AuthResult;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenExceptionHandler {

    public static void commence(HttpServletRequest request, HttpServletResponse response, TokenException exception) throws IOException {
        // 防止中文乱码
        response.setHeader("content-type","application/json;charset=utf-8");
        AuthResult result = AuthResult.error(ResponseStatus.UNAUTHORIZED, exception.getMessage());
        response.getWriter().println(JSON.toJSONString(result));
        log.warn(exception.getMessage());
    }
}
