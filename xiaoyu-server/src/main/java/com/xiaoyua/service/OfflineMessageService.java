package com.xiaoyua.service;

import com.xiaoyua.entity.OfflineMessagePO;
import com.xiaoyua.mq.message.NotificationMessage;
import com.xiaoyua.mq.message.PrivateMessage;

import java.util.List;

/**
 * 离线消息服务接口
 * 
 * @author xiaoyu
 */
public interface OfflineMessageService {
    
    /**
     * 存储离线通知消息
     * 
     * @param message 通知消息
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean storeOfflineNotification(NotificationMessage message, Long notificationId);
    
    /**
     * 存储离线私信消息
     * 
     * @param message 私信消息
     * @return 是否成功
     */
    boolean storeOfflinePrivateMessage(PrivateMessage message);
    
    /**
     * 获取用户的离线消息
     * 
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 离线消息列表
     */
    List<OfflineMessagePO> getUserOfflineMessages(Long userId, Integer limit);
    
    /**
     * 标记离线消息为已推送
     * 
     * @param messageIds 消息ID列表
     * @return 更新数量
     */
    int markMessagesAsPushed(List<Long> messageIds);
    
    /**
     * 清理过期的离线消息
     * 
     * @return 清理数量
     */
    int cleanExpiredMessages();
    
    /**
     * 获取用户未推送的离线消息数量
     * 
     * @param userId 用户ID
     * @return 未推送消息数量
     */
    Long getUserPendingMessageCount(Long userId);
    
    /**
     * 用户上线时推送离线消息
     * 
     * @param userId 用户ID
     * @return 推送的消息数量
     */
    int pushOfflineMessagesOnUserOnline(Long userId);
}
