package com.xiaoyua.websocket;

import com.xiaoyua.service.OfflineMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户上线事件处理器
 * 负责处理用户上线时的离线消息推送
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class UserOnlineEventHandler {
    
    @Autowired
    private OfflineMessageService offlineMessageService;
    
    /**
     * 处理用户上线事件
     * 
     * @param userId 用户ID
     */
    public void handleUserOnline(Long userId) {
        try {
            log.info("处理用户上线事件: userId={}", userId);
            
            // 推送离线消息
            int pushedCount = offlineMessageService.pushOfflineMessagesOnUserOnline(userId);
            
            if (pushedCount > 0) {
                log.info("用户上线推送离线消息完成: userId={}, pushedCount={}", userId, pushedCount);
            } else {
                log.debug("用户上线无离线消息需要推送: userId={}", userId);
            }
            
        } catch (Exception e) {
            log.error("处理用户上线事件失败: userId={}, error={}", userId, e.getMessage(), e);
        }
    }
    
    /**
     * 获取用户未读离线消息数量
     * 
     * @param userId 用户ID
     * @return 未读消息数量
     */
    public Long getUserPendingMessageCount(Long userId) {
        try {
            return offlineMessageService.getUserPendingMessageCount(userId);
        } catch (Exception e) {
            log.error("获取用户未读离线消息数量失败: userId={}, error={}", userId, e.getMessage(), e);
            return 0L;
        }
    }
}
