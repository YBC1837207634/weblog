package com.gong.blog.common.handler;

import com.gong.blog.common.constants.ResponseStatus;
import com.gong.blog.common.exception.*;
import com.gong.blog.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

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
     * 用户已存在
     */
    @ExceptionHandler(UserException.class)
    public Result<String> userException(UserException ex) {
        log.warn(ex.getMessage());
        return Result.error(ResponseStatus.CONFLICT, ex.getMessage());
    }


   @ExceptionHandler(SystemException.class)
    public Result<String> systemException(SystemException ex) {
        log.warn(ex.getMessage());
        return Result.error(ResponseStatus.WARN, ex.getMessage());
    }

    /**
     * 未找到资源
     * @param ex
     * @return
     */
    @ExceptionHandler(FileNotFoundException.class)
    public Result<String> notFound(FileNotFoundException ex) {
        log.warn(ex.getMessage());
        return Result.error(ResponseStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(NotHaveDataException.class)
    public Result<String> notHaveData(NotHaveDataException ex) {
        log.warn(ex.getMessage());
        return Result.error(ResponseStatus.NOT_DATA, ex.getMessage());
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public Result<String> fileMaxEx(FileSizeLimitExceededException e) {
        log.warn(e.getMessage());
        return Result.error(ResponseStatus.BAD_REQUEST, "超出文件大小限制");
    }

    /**
     * 参数不正确
     * */
    @ExceptionHandler(ParamException.class)
    public Result<String> paramException(ParamException e) {
        log.warn(e.getMessage());
        return Result.error(ResponseStatus.BAD_REQUEST, e.getMessage());
    }

    /**
     * 数据库 完整性约束
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> Violation(Exception ex) {
        log.warn(ex.getMessage());
        return Result.error(ResponseStatus.INTERNAL, "操作有误！");
    }

    /**
     * 非法请求
     */
    @ExceptionHandler(UnlawfulRequestException.class)
    public Result<String> argument(UnlawfulRequestException exception)  {
        log.warn(exception.getMessage());
        return Result.error(ResponseStatus.BAD_REQUEST, exception.getMessage());
    }

}
