package com.gong.weblog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTUtils {

    //秘钥
    public static final String SECRET = "giaogaio";

    public static String createToken(String userId) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            Map<String, Object> m = new HashMap<>();
            m.put("alg", "HS256");
            m.put("typ", "JWT");
            //签名时间
            Date nowDate = new Date();
            //过期时间Date对象
            Date expire = new Date(System.currentTimeMillis() + 1000 * 36000);
            String token = JWT.create().
                    //设置头部
                            withHeader(m)
                    // 设置 载荷 Payload
                    .withClaim("userId", userId)
                    //签名时间
                    .withIssuedAt(nowDate)
                    //过期时间
                    .withExpiresAt(expire)
                    .sign(algorithm);
            return token;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 验证token
     * @return
     */
    public static Map<String, Claim> verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier build = JWT.require(algorithm)
                    .build();
            DecodedJWT verify = build.verify(token);
            //获取声明信息
            Map<String, Claim> claims = verify.getClaims();
            //转为字符串
            return claims;
        }catch (Exception e){
            log.warn(e.getMessage());
        }
        return null;
    }


}
