package com.gong.weblog.interceptor;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.gong.weblog.common.ResponseStatus;
import com.gong.weblog.service.UserService;
import com.gong.weblog.utils.JWTUtils;
import com.gong.weblog.utils.UserContextUtils;
import com.gong.weblog.vo.AuthResult;
import com.gong.weblog.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String message;
        if (Objects.nonNull(token) && StringUtils.hasText(token)) {
            Map<String, Claim> claimMap = JWTUtils.verifyToken(token);;
            if (Objects.nonNull(claimMap)) {
                // 查找数据库
                String userId = claimMap.get("userId").asString();
                UserVo user = userService.getUserVo(Long.valueOf(userId));
                if (Objects.nonNull(user) && user.getStatus().equals("1")) {
                    UserContextUtils.setUser(user);
                    return true;
                }
            }
            message = "token无效";
        } else {
            // 没有携带token
            message = "未携带令牌，拒绝访问";
        }
        // 防止中文乱码
        response.setHeader("content-type","application/json;charset=utf-8");
        AuthResult result = AuthResult.error(ResponseStatus.UNAUTHORIZED, message);
        response.getWriter().println(JSON.toJSONString(result));
        log.warn(message);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextUtils.remove();
        log.info("用户信息清空");
    }
}
