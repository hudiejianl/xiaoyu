package com.xiaoyua.task;

import com.xiaoyua.service.OfflineMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 离线消息清理定时任务
 * 
 * @author xiaoyu
 */
@Component
@Slf4j
public class OfflineMessageCleanupTask {
    
    @Autowired
    private OfflineMessageService offlineMessageService;
    
    /**
     * 清理过期的离线消息
     * 每小时执行一次
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void cleanExpiredOfflineMessages() {
        try {
            log.info("开始清理过期离线消息");
            
            int cleanedCount = offlineMessageService.cleanExpiredMessages();
            
            if (cleanedCount > 0) {
                log.info("清理过期离线消息完成: 清理数量={}", cleanedCount);
            } else {
                log.debug("没有过期的离线消息需要清理");
            }
            
        } catch (Exception e) {
            log.error("清理过期离线消息失败: error={}", e.getMessage(), e);
        }
    }
}
