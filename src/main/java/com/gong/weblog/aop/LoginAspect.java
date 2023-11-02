package com.gong.weblog.aop;

import com.gong.weblog.dto.LoginForm;
import com.gong.weblog.entity.WeblogLogininfor;
import com.gong.weblog.service.WeblogLogininforService;
import com.gong.weblog.utils.ServletUtils;
import com.gong.weblog.utils.ip.AddressUtils;
import com.gong.weblog.utils.ip.IpUtils;
import com.gong.weblog.vo.AuthResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class LoginAspect {

    @Autowired
    WeblogLogininforService logininforService;

    /**
     * 后置通知
     */
    @AfterReturning(value = "execution(* com.gong.weblog.controller.LoginController.login(*))", returning = "result")
    public void loginInfo(JoinPoint joinPoint, AuthResult result) {
        setInfo(joinPoint, result, null);
    }

    /**
     * 后置异常通知
     */
    @AfterThrowing(value = "execution(* com.gong.weblog.controller.LoginController.login(*))", throwing = "ex")
    public void LoginError(JoinPoint joinPoint, Exception ex) {
        setInfo(joinPoint, null, ex);
    }

    protected void setInfo(JoinPoint joinPoint, AuthResult result, Exception ex) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            Object[] args = joinPoint.getArgs();
            WeblogLogininfor logininfor = new WeblogLogininfor();
            // 设置登陆用户名
            if (args.length == 1 && args[0] instanceof LoginForm loginForm) {
                logininfor.setUserName(loginForm.getUsername());
            }
            // 登录IP地址
            logininfor.setIpaddr(IpUtils.getIpAddr());
            // 登录地点
            logininfor.setLoginLocation(AddressUtils.getRealAddressByIP(IpUtils.getIpAddr()));
            // 浏览器类型
            logininfor.setBrowser(getBrowserType(request));
            // 操作系统
            logininfor.setOs(getOperatingSystem(request));
            // 登录状态
            // 提示消息
            if (Objects.isNull(ex) && Objects.nonNull(result)) {
                if (result.getCode() == 200) {
                    logininfor.setStatus("1");
                    logininfor.setMsg(result.getMsg());
                } else {
                    logininfor.setStatus("0");
                    logininfor.setMsg(result.getMsg());
                }
            } else if (Objects.nonNull(ex)) {
                logininfor.setStatus("0");
                logininfor.setMsg(ex.getMessage());
            }
            // 访问时间
            logininfor.setLoginTime(LocalDateTime.now());
            logininforService.save(logininfor);
        } catch (Exception e) {
            log.error("登陆日志记录时出现错误 {}", e.getMessage());
        }
    }


    protected String getBrowserType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.contains("MSIE")) {
            return "Internet Explorer";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Opera")) {
            return "Opera";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        }
        return "Unknown";
    }

    protected String getOperatingSystem(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        // 根据User-Agent判断操作系统
        if (userAgent.toLowerCase().contains("windows")) {
            return "Windows";
        } else if (userAgent.toLowerCase().contains("mac")) {
            return "Mac";
        } else if (userAgent.toLowerCase().contains("linux")) {
            return "Linux";
        } else if (userAgent.toLowerCase().contains("android")) {
            return "Android";
        } else if (userAgent.toLowerCase().contains("iphone") || userAgent.toLowerCase().contains("ipad")) {
            return "iOS";
        } else {
            return "Unknown";
        }
    }
}
