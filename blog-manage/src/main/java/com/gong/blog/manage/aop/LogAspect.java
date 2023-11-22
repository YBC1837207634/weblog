package com.gong.blog.manage.aop;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.gong.blog.common.annotation.Log;
import com.gong.blog.common.utils.ServletUtils;
import com.gong.blog.common.utils.ip.AddressUtils;
import com.gong.blog.common.utils.ip.IpUtils;
import com.gong.blog.manage.dto.SysUserDTO;
import com.gong.blog.manage.entity.SysOperLog;
import com.gong.blog.manage.service.SysOperLogService;
import com.gong.blog.manage.utils.CustomUserDetailsUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 切面类 记录操作日志
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private SysOperLogService operLogService;

    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Before("@annotation(com.gong.blog.common.annotation.Log)")
    public void beforeLog() {
        startTime.set(System.currentTimeMillis());
    }

    /**
     * 业务完成后
     */
    @AfterReturning(value = "@annotation(controllerLog)",returning = "res")
    public void successLog(JoinPoint joinPoint, Log controllerLog, Object res) {
        setLog(joinPoint, controllerLog, res, null);
    }

    /**
     * 异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "ex")
    public void errorLog(JoinPoint joinPoint, Log controllerLog, Exception ex) {
        setLog(joinPoint, controllerLog, null, ex);
    }

    protected void setLog(JoinPoint joinPoint, Log controllerLog, Object res, Exception ex) {
        HttpServletRequest request = ServletUtils.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SysUserDTO user = CustomUserDetailsUtils.getUser();
        try {
            SysOperLog operLog = new SysOperLog();
            if (user!=null) {
                operLog.setOperName(user.getUsername());
            }
            // 日志标题
            operLog.setTitle(controllerLog.title());
            // 方法名称
            operLog.setMethod(signature.toString());
            // 业务类型
            operLog.setBusinessType(controllerLog.businessType().ordinal());
            // 方法
            operLog.setRequestMethod(request.getMethod());
            // 操作类别
            operLog.setOperatorType(controllerLog.operatorType().ordinal());
            // 请求url
            operLog.setOperUrl(request.getRequestURI());
            // 请求ip
            operLog.setOperIp(IpUtils.getIpAddr());
            // 操作地点
            operLog.setOperLocation(AddressUtils.getRealAddressByIP(IpUtils.getIpAddr()));
            // 错误信息
            if (Objects.nonNull(ex)) {
                operLog.setStatus(0);
                operLog.setErrorMsg(ex.getMessage());
            } else {
                operLog.setStatus(1);
            }
            if (controllerLog.isParams()) {
                // 设置日志请求参数
                setOperParams(joinPoint, request, operLog);
            }
            if (controllerLog.isResult() && Objects.nonNull(res)) {
                // 设置响应参数
                operLog.setJsonResult(StrUtil.sub(JSON.toJSONString(res), 0, 2000));
            }
            operLog.setOperTime(LocalDateTime.now());
            operLog.setCostTime(System.currentTimeMillis()-startTime.get());
            operLogService.saveOne(operLog);
        } catch (Exception e) {
            log.warn("日志记录异常");
        } finally {
            startTime.remove();
        }
    }

    /**
     * 设置日志参数
     */
    protected void setOperParams(JoinPoint joinPoint, HttpServletRequest request, SysOperLog operLog) {
        String params ;
        if (request.getMethod().equals(HttpMethod.POST.name()) || request.getMethod().equals(HttpMethod.PUT.name())) {
            params = getParams(joinPoint.getArgs());
        } else {
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (Objects.nonNull(parameterMap) && !parameterMap.isEmpty()) {
                params = JSON.toJSONString(parameterMap);
            } else {
                return;
            }
        }
        operLog.setOperParam(StrUtil.sub(params, 0, 2000));
    }


    /**
     * 将参数对象转换为字符串
     * @param args
     * @return
     */
    protected String getParams(Object[] args) {
        StringBuilder params = new StringBuilder();
        if (Objects.nonNull(args)) {
            for (Object o : args) {
                if (Objects.nonNull(o)) {
                    // 检查是否是 MultipartFile
                    if (o instanceof MultipartFile) {
                        return "File";
                    }
                    try {
                        String str = JSON.toJSONString(o);
                        params.append(str + " ");
                    } catch (Exception e) {
                        log.warn("转换为JSON时出错");
                    }
                }
            }
        }
        return params.toString().trim();
    }


}
