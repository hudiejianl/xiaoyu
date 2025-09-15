package com.xiaoyu.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket会话管理器
 * 统一管理所有WebSocket连接，支持消息和通知
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class WebSocketSessionManager {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private UserOnlineEventHandler userOnlineEventHandler;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 存储用户ID与WebSocket会话的映射
    private static final Map<Long, WebSocketSession> USER_SESSIONS = new ConcurrentHashMap<>();
    
    /**
     * 添加用户会话
     */
    public void addUserSession(Long userId, WebSocketSession session) {
        USER_SESSIONS.put(userId, session);
        // 在Redis中标记用户在线
        redisTemplate.opsForValue().set("user:online:" + userId, "1");
        log.info("用户 {} WebSocket连接建立", userId);
        
        // 触发用户上线事件，推送离线消息
        userOnlineEventHandler.handleUserOnline(userId);
    }
    
    /**
     * 移除用户会话
     */
    public void removeUserSession(Long userId) {
        USER_SESSIONS.remove(userId);
        // 从Redis中移除在线状态
        redisTemplate.delete("user:online:" + userId);
        log.info("用户 {} WebSocket连接关闭", userId);
    }
    
    /**
     * 获取用户会话
     */
    public WebSocketSession getUserSession(Long userId) {
        return USER_SESSIONS.get(userId);
    }
    
    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(Long userId) {
        return USER_SESSIONS.containsKey(userId) && 
               Boolean.TRUE.equals(redisTemplate.hasKey("user:online:" + userId));
    }
    
    /**
     * 向指定用户转发消息（纯转发，不处理业务逻辑）
     */
    public boolean forwardMessageToUser(Long userId, Object message) {
        WebSocketSession session = USER_SESSIONS.get(userId);
        if (session != null && session.isOpen()) {
            try {
                String messageJson = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(messageJson));
                return true;
            } catch (Exception e) {
                log.error("向用户 {} 转发消息失败: {}", userId, e.getMessage());
            }
        }
        return false;
    }
    
    /**
     * 发送错误消息
     */
    public void sendErrorMessage(WebSocketSession session, String errorMsg) {
        try {
            String errorJson = objectMapper.writeValueAsString(Map.of(
                "type", "error",
                "message", errorMsg
            ));
            session.sendMessage(new TextMessage(errorJson));
        } catch (IOException e) {
            log.error("发送错误消息失败: {}", e.getMessage());
        }
    }
    
    /**
     * 获取在线用户数量
     */
    public int getOnlineUserCount() {
        return USER_SESSIONS.size();
    }
    
    /**
     * 广播消息给所有在线用户
     */
    public void broadcastMessage(Object message) {
        USER_SESSIONS.forEach((userId, session) -> {
            if (session.isOpen()) {
                forwardMessageToUser(userId, message);
            }
        });
    }
}
