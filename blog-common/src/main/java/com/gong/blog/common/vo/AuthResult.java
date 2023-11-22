package com.gong.blog.common.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gong.blog.common.common.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * 认证结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResult {

    private int code;

    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    public static AuthResult success(String msg, String token) {
        return new AuthResult(ResponseStatus.SUCCESS, msg, token);
    }

    public static AuthResult error(int code, String msg) {
        return new AuthResult(code, msg, null);
    }
}
