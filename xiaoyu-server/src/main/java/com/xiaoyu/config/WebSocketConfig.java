package com.xiaoyu.config;

import com.xiaoyu.websocket.MessageWebSocketHandler;
import com.xiaoyu.websocket.NotificationWebSocketHandler;
import com.xiaoyu.websocket.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket配置类
 * 
 * @author xiaoyu
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    @Autowired
    private MessageWebSocketHandler messageWebSocketHandler;
    
    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;
    
    @Autowired
    private WebSocketInterceptor webSocketInterceptor;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册消息WebSocket处理器
        registry.addHandler(messageWebSocketHandler, "/ws/messages")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
        
        // 注册通知WebSocket处理器
        registry.addHandler(notificationWebSocketHandler, "/ws/notifications")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }
}
