package com.gong.blog.core.config;

import com.gong.blog.core.handler.WebSocketHandlerImpl;
import com.gong.blog.core.interceptor.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

//配置类
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private WebSocketInterceptor webSocketInterceptor;

    @Autowired
    private WebSocketHandlerImpl webSocketHandler;

    @Bean
    //注入ServerEndpointExporter bean对象，检测使用注解@ServerEndpoint的bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
            .addHandler(webSocketHandler, "/ws/whisper")
            .addInterceptors(webSocketInterceptor)
            .setAllowedOrigins("*");
    }
}