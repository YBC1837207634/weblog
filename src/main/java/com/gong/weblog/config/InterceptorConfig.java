package com.gong.weblog.config;

import com.gong.weblog.interceptor.AnonymousInterceptor;
import com.gong.weblog.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private AnonymousInterceptor anonymousInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(
                        "/login", "/register", "/tag/**", "/file/*", "/article/list", "/article/content/*",
                        "/doc.html","/doc.html/**"

                )
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/v2/**")
                .excludePathPatterns("/swagger-ui.html/**");
        registry.addInterceptor(anonymousInterceptor).addPathPatterns("/article/list", "/article/content/*");
    }


    // 资源映射增加
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}