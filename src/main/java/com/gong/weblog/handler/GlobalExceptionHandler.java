package com.gong.weblog.handler;

import com.gong.weblog.common.ResponseStatus;
import com.gong.weblog.exception.CUDException;
import com.gong.weblog.exception.ExistException;
import com.gong.weblog.exception.UserException;
import com.gong.weblog.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalHandler {


    /**
     * 数据校验出错
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> argument(MethodArgumentNotValidException exception)  {
        BindingResult bindingResult = exception.getBindingResult();
        log.warn(bindingResult.getFieldError().toString());
        return Result.error(ResponseStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
    }

    /**
     * 请求方式错误
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<String> badMethod(Exception ex) {
        log.warn(ex.getMessage());
        return Result.error(ResponseStatus.BAD_METHOD, "请求方式不正确");
    }

    /**
     * 请求错误 求传来的参数不符合要求
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, NullPointerException.class})
    public Result<String> badRequest(Exception ex) {
        log.warn(ex.getMessage());
        return Result.error(ResponseStatus.BAD_REQUEST, "请求不符合要求");
    }

    /**
     * 增删改异常
     * @return
     */
    @ExceptionHandler(CUDException.class)
    public Result<String> cudEx(CUDException ex) {
        log.warn(ex.getMessage());
        return Result.error(ResponseStatus.NOT_MODIFY, ex.getMessage());
    }

    /**
     * 字段已存在
     */
    @ExceptionHandler(ExistException.class)
    public Result<String> existException(ExistException ex) {
        log.warn(ex.getMessage());
        return Result.error(ResponseStatus.CONFLICT, ex.getMessage());
    }

     /**
     * 字段已存在
     */
    @ExceptionHandler(UserException.class)
    public Result<String> userException(ExistException ex) {
        log.warn(ex.getMessage());
        return Result.error(ResponseStatus.CONFLICT, ex.getMessage());
    }



}
