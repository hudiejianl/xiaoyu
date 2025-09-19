package com.xiaoyua.websocket;

import com.xiaoyua.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket握手拦截器
 * 用于JWT认证
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                 WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        String uri = request.getURI().toString();
        log.info("WebSocket握手请求: {}", uri);
        
        // 从查询参数中获取token
        String query = request.getURI().getQuery();
        if (query != null && query.contains("token=")) {
            String token = extractToken(query);
            if (token != null) {
                try {
                    // 验证JWT token
                    Long userId = jwtUtil.getUserIdFromToken(token);
                    if (userId != null) {
                        // 将用户ID存储到WebSocket会话属性中
                        attributes.put("userId", userId);
                        log.info("WebSocket认证成功，用户ID: {}", userId);
                        return true;
                    }
                } catch (Exception e) {
                    log.error("WebSocket JWT验证失败: {}", e.getMessage());
                }
            }
        }
        
        log.warn("WebSocket认证失败，拒绝连接");
        return false;
    }
    
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                             WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("WebSocket握手后异常: {}", exception.getMessage());
        }
    }
    
    /**
     * 从查询字符串中提取token
     */
    private String extractToken(String query) {
        String[] params = query.split("&");
        for (String param : params) {
            if (param.startsWith("token=")) {
                return param.substring(6); // "token=".length()
            }
        }
        return null;
    }
}
