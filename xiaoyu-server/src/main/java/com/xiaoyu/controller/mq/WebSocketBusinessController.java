package com.xiaoyu.controller.mq;

import com.xiaoyu.context.BaseContext;
import com.xiaoyu.service.NotificationService;
import com.xiaoyu.websocket.UserOnlineEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * WebSocket业务处理控制器
 * 处理之前在WebSocket中的业务逻辑，现在通过REST API处理
 * 
 * @author xiaoyu
 */
@RestController
@RequestMapping("/api/websocket")
@Slf4j
public class WebSocketBusinessController {
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private UserOnlineEventHandler userOnlineEventHandler;
    
    /**
     * 标记通知为已读
     */
    @PostMapping("/notifications/{notificationId}/read")
    public ResponseEntity<Map<String, Object>> markNotificationAsRead(@PathVariable Long notificationId) {
        try {
            Long userId = BaseContext.getCurrentId();
            boolean success = notificationService.markAsRead(userId, notificationId);
            
            return ResponseEntity.ok(Map.of(
                "success", success,
                "message", success ? "标记成功" : "标记失败",
                "notification_id", notificationId
            ));
        } catch (Exception e) {
            log.error("标记通知已读失败: notificationId={}, error={}", notificationId, e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "标记失败: " + e.getMessage(),
                "notification_id", notificationId
            ));
        }
    }
    
    /**
     * 获取未读通知数量
     */
    @GetMapping("/notifications/unread-count")
    public ResponseEntity<Map<String, Object>> getUnreadNotificationCount() {
        try {
            Long userId = BaseContext.getCurrentId();
            long unreadCount = notificationService.getUnreadCount(userId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", unreadCount
            ));
        } catch (Exception e) {
            log.error("获取未读通知数量失败: error={}", e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "获取失败: " + e.getMessage(),
                "count", 0
            ));
        }
    }
    
    /**
     * 标记所有通知为已读
     */
    @PostMapping("/notifications/read-all")
    public ResponseEntity<Map<String, Object>> markAllNotificationsAsRead() {
        try {
            Long userId = BaseContext.getCurrentId();
            int updatedCount = notificationService.markAllAsRead(userId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "updated_count", updatedCount,
                "message", "标记成功"
            ));
        } catch (Exception e) {
            log.error("标记所有通知已读失败: error={}", e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "标记失败: " + e.getMessage(),
                "updated_count", 0
            ));
        }
    }
    
    /**
     * 获取用户离线消息数量
     */
    @GetMapping("/offline-messages/count")
    public ResponseEntity<Map<String, Object>> getOfflineMessageCount() {
        try {
            Long userId = BaseContext.getCurrentId();
            Long count = userOnlineEventHandler.getUserPendingMessageCount(userId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "count", count
            ));
        } catch (Exception e) {
            log.error("获取离线消息数量失败: error={}", e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "获取失败: " + e.getMessage(),
                "count", 0
            ));
        }
    }
    
    /**
     * 手动触发离线消息推送
     */
    @PostMapping("/offline-messages/push")
    public ResponseEntity<Map<String, Object>> pushOfflineMessages() {
        try {
            Long userId = BaseContext.getCurrentId();
            userOnlineEventHandler.handleUserOnline(userId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "离线消息推送已触发"
            ));
        } catch (Exception e) {
            log.error("触发离线消息推送失败: error={}", e.getMessage(), e);
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "推送失败: " + e.getMessage()
            ));
        }
    }
}
