package com.gong.weblog.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.gong.weblog.entity.User;
import com.gong.weblog.service.UserService;
import com.gong.weblog.utils.JWTUtils;
import com.gong.weblog.utils.UserContextUtils;
import com.gong.weblog.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

@Component
public class AnonymousInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextUtils.remove();
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (Objects.nonNull(token) && StringUtils.hasText(token)) {
            Map<String, Claim> claimMap = JWTUtils.verifyToken(token);
            if (Objects.nonNull(claimMap)) {
                // 查找数据库
                String userId = claimMap.get("userId").asString();
                UserVo user = userService.getUserVo(Long.valueOf(userId));
                if (Objects.nonNull(user) && user.getStatus().equals("1")) {
                    UserContextUtils.setUser(user);
                    return true;
                }
            }
            // 匿名用户
        }
        UserVo userVo = new UserVo();
        UserContextUtils.setUser(userVo);
        return true;
    }
}
