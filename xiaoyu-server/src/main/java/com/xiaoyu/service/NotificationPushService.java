package com.xiaoyu.service;

import com.xiaoyu.entity.NotificationPO;
import com.xiaoyu.vo.notification.NotificationVO;

/**
 * 通知推送服务接口
 * 负责实时推送通知到在线用户
 * 
 * @author xiaoyu
 */
public interface NotificationPushService {
    
    /**
     * 推送通知给指定用户
     * 
     * @param userId 用户ID
     * @param notification 通知对象
     * @return 是否推送成功
     */
    boolean pushNotificationToUser(Long userId, NotificationVO notification);
    
    /**
     * 推送通知给指定用户（使用PO对象）
     * 
     * @param userId 用户ID
     * @param notificationPO 通知PO对象
     * @return 是否推送成功
     */
    boolean pushNotificationToUser(Long userId, NotificationPO notificationPO);
    
    /**
     * 推送未读通知数量更新
     * 
     * @param userId 用户ID
     * @param unreadCount 未读数量
     * @return 是否推送成功
     */
    boolean pushUnreadCountUpdate(Long userId, Integer unreadCount);
    
    /**
     * 检查用户是否在线
     * 
     * @param userId 用户ID
     * @return 是否在线
     */
    boolean isUserOnline(Long userId);
    
    /**
     * 推送系统通知给所有在线用户
     * 
     * @param notification 系统通知
     */
    void pushSystemNotification(NotificationVO notification);
    
    /**
     * 推送通知已读状态更新
     * 
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 是否推送成功
     */
    boolean pushNotificationReadUpdate(Long userId, Long notificationId);
    
    /**
     * 推送批量通知已读状态更新
     * 
     * @param userId 用户ID
     * @return 是否推送成功
     */
    boolean pushAllNotificationsReadUpdate(Long userId);
}
