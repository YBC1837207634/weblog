package com.gong.blog.common.exception;

/**
 * 非法请求
 */
public class UnlawfulRequestException extends RuntimeException{
    public UnlawfulRequestException(String mes) {
        super(mes);
    }
}
