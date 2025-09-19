package com.xiaoyua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoyua.entity.OfflineMessagePO;
import com.xiaoyua.mapper.OfflineMessageMapper;
import com.xiaoyua.mq.message.NotificationMessage;
import com.xiaoyua.mq.message.PrivateMessage;
import com.xiaoyua.service.OfflineMessageService;
import com.xiaoyua.websocket.UnifiedWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 离线消息服务实现类
 * 
 * @author xiaoyu
 */
@Service
@Slf4j
public class OfflineMessageServiceImpl implements OfflineMessageService {
    
    @Autowired
    private OfflineMessageMapper offlineMessageMapper;
    
    @Autowired
    private UnifiedWebSocketHandler webSocketHandler;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public boolean storeOfflineNotification(NotificationMessage message, Long notificationId) {
        try {
            OfflineMessagePO offlineMessage = new OfflineMessagePO();
            offlineMessage.setUserId(message.getUserId());
            offlineMessage.setMessageType("NOTIFICATION");
            offlineMessage.setNotificationId(notificationId);
            offlineMessage.setFromUserId(message.getFromUserId());
            offlineMessage.setStatus("PENDING");
            offlineMessage.setRetryCount(0);
            offlineMessage.setExpireAt(LocalDateTime.now().plusDays(7)); // 7天后过期
            
            // 将消息内容序列化为JSON
            Map<String, Object> content = new HashMap<>();
            content.put("type", "notification");
            content.put("id", notificationId);
            content.put("notification_type", message.getType());
            content.put("title", message.getTitle());
            content.put("content", message.getContent());
            content.put("ref_id", message.getRefId());
            content.put("ref_type", message.getRefType());
            content.put("from_user_id", message.getFromUserId());
            content.put("from_user_nickname", message.getFromUserNickname());
            content.put("from_user_avatar", message.getFromUserAvatar());
            content.put("created_at", message.getCreatedAt().toString());
            
            offlineMessage.setMessageContent(objectMapper.writeValueAsString(content));
            
            int result = offlineMessageMapper.insert(offlineMessage);
            
            log.info("存储离线通知消息: userId={}, notificationId={}, result={}", 
                    message.getUserId(), notificationId, result > 0 ? "成功" : "失败");
            
            return result > 0;
            
        } catch (JsonProcessingException e) {
            log.error("序列化离线通知消息失败: userId={}, notificationId={}, error={}", 
                    message.getUserId(), notificationId, e.getMessage(), e);
            return false;
        } catch (Exception e) {
            log.error("存储离线通知消息失败: userId={}, notificationId={}, error={}", 
                    message.getUserId(), notificationId, e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean storeOfflinePrivateMessage(PrivateMessage message) {
        try {
            OfflineMessagePO offlineMessage = new OfflineMessagePO();
            offlineMessage.setUserId(message.getToUserId());
            offlineMessage.setMessageType("PRIVATE_MESSAGE");
            offlineMessage.setOriginalMessageId(message.getOriginalMessageId());
            offlineMessage.setFromUserId(message.getFromUserId());
            offlineMessage.setStatus("PENDING");
            offlineMessage.setRetryCount(0);
            offlineMessage.setExpireAt(LocalDateTime.now().plusDays(30)); // 30天后过期
            
            // 将消息内容序列化为JSON
            Map<String, Object> content = new HashMap<>();
            content.put("type", "private_message");
            content.put("message_id", message.getOriginalMessageId());
            content.put("from_user_id", message.getFromUserId());
            content.put("from_user_nickname", message.getFromUserNickname());
            content.put("from_user_avatar", message.getFromUserAvatar());
            content.put("content", message.getContent());
            content.put("message_type", message.getMessageType());
            content.put("created_at", message.getCreatedAt().toString());
            
            offlineMessage.setMessageContent(objectMapper.writeValueAsString(content));
            
            int result = offlineMessageMapper.insert(offlineMessage);
            
            log.info("存储离线私信消息: toUserId={}, originalMessageId={}, result={}", 
                    message.getToUserId(), message.getOriginalMessageId(), result > 0 ? "成功" : "失败");
            
            return result > 0;
            
        } catch (JsonProcessingException e) {
            log.error("序列化离线私信消息失败: toUserId={}, originalMessageId={}, error={}", 
                    message.getToUserId(), message.getOriginalMessageId(), e.getMessage(), e);
            return false;
        } catch (Exception e) {
            log.error("存储离线私信消息失败: toUserId={}, originalMessageId={}, error={}", 
                    message.getToUserId(), message.getOriginalMessageId(), e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public List<OfflineMessagePO> getUserOfflineMessages(Long userId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 50; // 默认限制50条
        }
        return offlineMessageMapper.selectOfflineMessages(userId, "PENDING", limit);
    }
    
    @Override
    public int markMessagesAsPushed(List<Long> messageIds) {
        if (messageIds == null || messageIds.isEmpty()) {
            return 0;
        }
        return offlineMessageMapper.batchUpdateStatus(messageIds, "PUSHED", 0);
    }
    
    @Override
    public int cleanExpiredMessages() {
        LocalDateTime expireTime = LocalDateTime.now();
        int count = offlineMessageMapper.expireOldMessages(expireTime);
        if (count > 0) {
            log.info("清理过期离线消息: count={}", count);
        }
        return count;
    }
    
    @Override
    public Long getUserPendingMessageCount(Long userId) {
        return offlineMessageMapper.countPendingMessages(userId);
    }
    
    @Override
    public int pushOfflineMessagesOnUserOnline(Long userId) {
        try {
            // 获取用户的离线消息
            List<OfflineMessagePO> offlineMessages = getUserOfflineMessages(userId, 100);
            
            if (offlineMessages.isEmpty()) {
                return 0;
            }
            
            int pushedCount = 0;
            List<Long> pushedMessageIds = offlineMessages.stream()
                    .filter(msg -> {
                        try {
                            // 解析消息内容并推送
                            @SuppressWarnings("unchecked")
                            Map<String, Object> content = objectMapper.readValue(msg.getMessageContent(), Map.class);
                            content.put("timestamp", System.currentTimeMillis());
                            
                            WebSocketSession session = webSocketHandler.getUserSession(userId);
                            boolean sent = false;
                            if (session != null && session.isOpen()) {
                                try {
                                    String messageJson = objectMapper.writeValueAsString(content);
                                    session.sendMessage(new TextMessage(messageJson));
                                    sent = true;
                                } catch (Exception e) {
                                    log.error("WebSocket发送离线消息失败: {}", e.getMessage());
                                }
                            }
                            if (sent) {
                                log.debug("推送离线消息成功: userId={}, messageId={}, type={}", 
                                        userId, msg.getId(), msg.getMessageType());
                                return true;
                            } else {
                                log.warn("推送离线消息失败: userId={}, messageId={}", userId, msg.getId());
                                return false;
                            }
                        } catch (Exception e) {
                            log.error("推送离线消息异常: userId={}, messageId={}, error={}", 
                                    userId, msg.getId(), e.getMessage(), e);
                            return false;
                        }
                    })
                    .map(OfflineMessagePO::getId)
                    .collect(Collectors.toList());
            
            // 标记已推送的消息
            if (!pushedMessageIds.isEmpty()) {
                pushedCount = markMessagesAsPushed(pushedMessageIds);
                log.info("用户上线推送离线消息完成: userId={}, totalMessages={}, pushedCount={}", 
                        userId, offlineMessages.size(), pushedCount);
            }
            
            return pushedCount;
            
        } catch (Exception e) {
            log.error("用户上线推送离线消息失败: userId={}, error={}", userId, e.getMessage(), e);
            return 0;
        }
    }
}
