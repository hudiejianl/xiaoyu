package com.xiaoyua.config;

import com.xiaoyua.websocket.UnifiedWebSocketHandler;
import com.xiaoyua.websocket.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebSocket配置类
 * 现在支持通过WebSocket直接发送私信消息，提高实时性
 * 
 * @author xiaoyu
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    @Autowired
    private UnifiedWebSocketHandler unifiedWebSocketHandler;
    
    @Autowired
    private WebSocketInterceptor webSocketInterceptor;
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册统一WebSocket处理器 - 只负责消息转发
        registry.addHandler(unifiedWebSocketHandler, "/ws")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
        
        // 保持向后兼容的路径
        registry.addHandler(unifiedWebSocketHandler, "/ws/messages")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
        
        registry.addHandler(unifiedWebSocketHandler, "/ws/notifications")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }
}
