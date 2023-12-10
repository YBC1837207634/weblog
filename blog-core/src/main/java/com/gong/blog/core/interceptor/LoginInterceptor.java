package com.gong.blog.core.interceptor;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.gong.blog.common.constants.ResponseStatus;
import com.gong.blog.common.utils.JWTUtils;
import com.gong.blog.common.utils.UserContextUtils;
import com.gong.blog.common.vo.AuthResult;
import com.gong.blog.common.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String message;
        if (Objects.nonNull(token) && StringUtils.hasText(token)) {
            Map<String, Claim> claimMap = JWTUtils.verifyToken(token);;
            if (Objects.nonNull(claimMap)) {
                //   查找用户
                ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                UserVo user = (UserVo) valueOperations.get("user:user_" + claimMap.get("username").asString());
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
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextUtils.remove();
    }
}
