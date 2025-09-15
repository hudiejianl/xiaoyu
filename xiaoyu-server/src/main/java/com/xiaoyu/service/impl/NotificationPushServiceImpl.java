package com.xiaoyu.service.impl;

import com.xiaoyu.entity.NotificationPO;
import com.xiaoyu.service.NotificationPushService;
import com.xiaoyu.service.NotificationService;
import com.xiaoyu.vo.notification.NotificationVO;
import com.xiaoyu.websocket.WebSocketSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知推送服务实现类
 * 
 * @author xiaoyu
 */
@Service
@Slf4j
public class NotificationPushServiceImpl implements NotificationPushService {
    
    @Autowired
    private WebSocketSessionManager sessionManager;
    
    @Autowired
    private NotificationService notificationService;
    
    @Override
    public boolean pushNotificationToUser(Long userId, NotificationVO notification) {
        if (userId == null || notification == null) {
            log.warn("推送通知参数为空: userId={}, notification={}", userId, notification);
            return false;
        }
        
        try {
            // 构建推送消息
            Map<String, Object> message = new HashMap<>();
            message.put("type", "notification");
            message.put("data", notification);
            message.put("timestamp", System.currentTimeMillis());
            
            // 通过WebSocket推送
            boolean success = sessionManager.sendMessageToUser(userId, message);
            
            if (success) {
                log.info("成功推送通知给用户 {}: {}", userId, notification.getTitle());
            } else {
                log.warn("用户 {} 不在线，通知推送失败", userId);
            }
            
            return success;
        } catch (Exception e) {
            log.error("推送通知给用户 {} 失败: {}", userId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean pushNotificationToUser(Long userId, NotificationPO notificationPO) {
        if (notificationPO == null) {
            return false;
        }
        
        // 转换为VO对象
        NotificationVO notificationVO = notificationService.convertToVO(notificationPO);
        return pushNotificationToUser(userId, notificationVO);
    }
    
    @Override
    public boolean pushUnreadCountUpdate(Long userId, Integer unreadCount) {
        if (userId == null || unreadCount == null) {
            return false;
        }
        
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "unread_count_update");
            message.put("count", unreadCount);
            message.put("timestamp", System.currentTimeMillis());
            
            boolean success = sessionManager.sendMessageToUser(userId, message);
            
            if (success) {
                log.info("成功推送未读数量更新给用户 {}: {}", userId, unreadCount);
            }
            
            return success;
        } catch (Exception e) {
            log.error("推送未读数量更新给用户 {} 失败: {}", userId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean isUserOnline(Long userId) {
        return sessionManager.isUserOnline(userId);
    }
    
    @Override
    public void pushSystemNotification(NotificationVO notification) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "system_notification");
            message.put("data", notification);
            message.put("timestamp", System.currentTimeMillis());
            
            sessionManager.broadcastMessage(message);
            log.info("成功广播系统通知: {}", notification.getTitle());
        } catch (Exception e) {
            log.error("广播系统通知失败: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public boolean pushNotificationReadUpdate(Long userId, Long notificationId) {
        if (userId == null || notificationId == null) {
            return false;
        }
        
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "notification_read_update");
            message.put("notification_id", notificationId);
            message.put("timestamp", System.currentTimeMillis());
            
            boolean success = sessionManager.sendMessageToUser(userId, message);
            
            if (success) {
                log.info("成功推送通知已读更新给用户 {}: notificationId={}", userId, notificationId);
            }
            
            return success;
        } catch (Exception e) {
            log.error("推送通知已读更新给用户 {} 失败: {}", userId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean pushAllNotificationsReadUpdate(Long userId) {
        if (userId == null) {
            return false;
        }
        
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "all_notifications_read_update");
            message.put("timestamp", System.currentTimeMillis());
            
            boolean success = sessionManager.sendMessageToUser(userId, message);
            
            if (success) {
                log.info("成功推送全部通知已读更新给用户 {}", userId);
            }
            
            return success;
        } catch (Exception e) {
            log.error("推送全部通知已读更新给用户 {} 失败: {}", userId, e.getMessage(), e);
            return false;
        }
    }
}
