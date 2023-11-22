package com.gong.blog.manage.filter;

import com.auth0.jwt.interfaces.Claim;
import com.gong.blog.common.exception.TokenException;
import com.gong.blog.common.utils.JWTUtils;
import com.gong.blog.manage.dto.SysUserDTO;
import com.gong.blog.manage.entity.SysRole;
import com.gong.blog.manage.entity.SysUser;
import com.gong.blog.manage.handler.TokenExceptionHandler;
import com.gong.blog.manage.service.SysUserService;
import com.gong.blog.manage.utils.CustomUserDetailsUtils;
import com.gong.blog.manage.vo.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * JWT 验证
 */
@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter {

    private SysUserService sysUserService;

    @Autowired
    public void setUserMapper(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (request.getRequestURI().contains("/login")
                || request.getRequestURI().contains("/register")
                || request.getRequestURI().contains("/file")) {
            filterChain.doFilter(request,response);
            return;
        }
        if (StringUtils.hasText(token)) {
            Map<String, Claim> claimMap = JWTUtils.verifyToken(token);;
            if (Objects.isNull(claimMap)) {
                TokenExceptionHandler.commence(request, response, new TokenException("token无效"));
                return;
            }
            // 查找数据库
            String userId = claimMap.get("userId").asString();
            SysUser user = sysUserService.getById(Long.valueOf(userId));
            if (Objects.isNull(user) || user.getStatus() == 0) {
                TokenExceptionHandler.commence(request, response, new TokenException("token失效"));
                return;
            }
            // 获取角色列表
            List<SysRole> roles = sysUserService.getSysRoleByUserId(user.getId());
            SysUserDTO userDTO = new SysUserDTO();
            BeanUtils.copyProperties(user,userDTO);
            // 不是管理员得到用户所对应的权限标识符列表
            List<String> purview = sysUserService.getPurviewByUserId(user.getId());
            userDTO.setRoles(roles);
            userDTO.setPurview(purview);
            // 创建一个 userDetails 存放到凭证中，用于后续使用
            CustomUserDetails userDetails = new CustomUserDetails(userDTO, purview);
            // 创建认证凭证 将凭证设置到 SecurityContext 中去，凭借该凭证通过后续的过滤链不需要再进行登陆验证
            CustomUserDetailsUtils.setCustomUserDetails(userDetails);
            filterChain.doFilter(request,response);
        } else {
            // 没有携带token
            TokenExceptionHandler.commence(request, response, new TokenException("未携带令牌"));
        }
    }
}
