package com.gong.blog.core.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.gong.blog.common.utils.JWTUtils;
import com.gong.blog.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Objects;

@Component
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 获取 query 参数，得到token
        MultiValueMap<String, String> queryParams = UriComponentsBuilder
                    .newInstance()
                    .query(request.getURI().getQuery())
                    .build()
                    .getQueryParams();

        String token = queryParams.get("access").get(0);

        if (!StringUtils.hasText(token)) {
            return false;
        }

        // 验证token的逻辑
        if (isValidToken(token, attributes)) {
            // 验证通过，将token存入attributes，供WebSocketHandshakeHandler使用
            return true;
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        // 这个方法在握手成功后调用，可以在这里进行一些后续处理

    }
    
    private boolean isValidToken(String token, Map<String, Object> attributes) {
        if (Objects.nonNull(token) && StringUtils.hasText(token)) {
            Map<String, Claim> claimMap = JWTUtils.verifyToken(token);
            if (Objects.nonNull(claimMap)) {
                //   查找用户
                ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                UserVo user = (UserVo) valueOperations.get("user:user_" + claimMap.get("username").asString());
                if (Objects.nonNull(user) && user.getStatus().equals("1")) {
                    attributes.put("uid", user.getId());
                    return true;
                }
            }
        }
        return false;
    }
}