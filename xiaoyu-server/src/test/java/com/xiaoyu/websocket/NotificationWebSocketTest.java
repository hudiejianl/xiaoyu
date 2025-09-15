package com.xiaoyu.websocket;

import com.xiaoyu.entity.NotificationPO;
import com.xiaoyu.service.NotificationPushService;
import com.xiaoyu.service.NotificationService;
import com.xiaoyu.vo.notification.NotificationVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * WebSocket通知功能测试
 * 
 * @author xiaoyu
 */
@SpringBootTest
@Slf4j
public class NotificationWebSocketTest {
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private NotificationPushService notificationPushService;
    
    @Autowired
    private WebSocketSessionManager sessionManager;
    
    /**
     * 测试创建通知并推送
     */
    @Test
    public void testCreateAndPushNotification() {
        log.info("开始测试创建通知并推送");
        
        // 创建测试通知
        NotificationPO notification = new NotificationPO();
        notification.setUserId(1L);
        notification.setType(NotificationPO.Type.LIKE);
        notification.setTitle("测试通知");
        notification.setContent("这是一个测试通知");
        notification.setRefId(100L);
        notification.setRefType(NotificationPO.RefType.POST);
        
        // 创建通知（会自动推送给在线用户）
        boolean success = notificationService.createNotification(notification);
        
        log.info("通知创建结果: {}", success ? "成功" : "失败");
        log.info("通知ID: {}", notification.getId());
    }
    
    /**
     * 测试推送服务
     */
    @Test
    public void testPushService() {
        log.info("开始测试推送服务");
        
        Long userId = 1L;
        
        // 检查用户是否在线
        boolean isOnline = notificationPushService.isUserOnline(userId);
        log.info("用户 {} 在线状态: {}", userId, isOnline);
        
        // 测试推送未读数量更新
        boolean pushSuccess = notificationPushService.pushUnreadCountUpdate(userId, 5);
        log.info("推送未读数量更新结果: {}", pushSuccess ? "成功" : "失败");
        
        // 测试推送通知已读更新
        pushSuccess = notificationPushService.pushNotificationReadUpdate(userId, 1L);
        log.info("推送通知已读更新结果: {}", pushSuccess ? "成功" : "失败");
    }
    
    /**
     * 测试WebSocket会话管理
     */
    @Test
    public void testSessionManager() {
        log.info("开始测试WebSocket会话管理");
        
        // 获取在线用户数量
        int onlineCount = sessionManager.getOnlineUserCount();
        log.info("当前在线用户数量: {}", onlineCount);
        
        // 测试用户在线状态
        Long userId = 1L;
        boolean isOnline = sessionManager.isUserOnline(userId);
        log.info("用户 {} 在线状态: {}", userId, isOnline);
    }
    
    /**
     * 测试系统通知广播
     */
    @Test
    public void testSystemNotificationBroadcast() {
        log.info("开始测试系统通知广播");
        
        // 创建系统通知VO
        NotificationVO systemNotification = new NotificationVO();
        systemNotification.setType("SYSTEM");
        systemNotification.setTitle("系统维护通知");
        systemNotification.setContent("系统将于今晚22:00-24:00进行维护，请提前保存数据");
        
        // 广播系统通知
        notificationPushService.pushSystemNotification(systemNotification);
        log.info("系统通知广播完成");
    }
}
