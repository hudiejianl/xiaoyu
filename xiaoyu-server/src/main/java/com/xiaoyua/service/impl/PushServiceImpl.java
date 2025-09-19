package com.xiaoyua.service.impl;

import com.xiaoyua.entity.UserPO;
import com.xiaoyua.mapper.UserMapper;
import com.xiaoyua.mq.message.NotificationMessage;
import com.xiaoyua.mq.message.PrivateMessage;
import com.xiaoyua.mq.producer.MessageProducer;
import com.xiaoyua.mq.producer.NotificationProducer;
import com.xiaoyua.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 独立推送服务实现类
 * 
 * @author xiaoyu
 */
@Service
@Slf4j
public class PushServiceImpl implements PushService {
    
    @Autowired
    private NotificationProducer notificationProducer;
    
    @Autowired
    private MessageProducer messageProducer;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public void pushNotification(Long userId, String type, String title, String content, 
                                Long refId, String refType, Long fromUserId) {
        try {
            // 不给自己发通知
            if (userId.equals(fromUserId)) {
                return;
            }
            
            NotificationMessage message = new NotificationMessage(
                userId, type, title, content, refId, refType, fromUserId
            );
            
            // 如果有发送者信息，填充发送者详情
            if (fromUserId != null) {
                UserPO fromUser = userMapper.selectById(fromUserId);
                if (fromUser != null) {
                    message.setFromUserNickname(fromUser.getNickname());
                    message.setFromUserAvatar(fromUser.getAvatarUrl());
                }
            }
            
            notificationProducer.sendNotificationPush(message);
            
            log.info("推送通知消息成功: userId={}, type={}, fromUserId={}", 
                    userId, type, fromUserId);
            
        } catch (Exception e) {
            log.error("推送通知消息失败: userId={}, type={}, error={}", 
                    userId, type, e.getMessage(), e);
        }
    }
    
    @Override
    public void pushNotification(Long userId, String type, String title, String content) {
        pushNotification(userId, type, title, content, null, null, null);
    }
    
    @Override
    public void pushPrivateMessage(Long originalMessageId, Long fromUserId, Long toUserId, 
                                  String content, String messageType) {
        try {
            PrivateMessage message = new PrivateMessage(
                originalMessageId, fromUserId, toUserId, content, messageType
            );
            
            // 填充发送者信息
            UserPO fromUser = userMapper.selectById(fromUserId);
            if (fromUser != null) {
                message.setFromUserNickname(fromUser.getNickname());
                message.setFromUserAvatar(fromUser.getAvatarUrl());
            }
            
            messageProducer.sendMessagePush(message);
            
            log.info("推送私信消息成功: originalMessageId={}, fromUserId={}, toUserId={}", 
                    originalMessageId, fromUserId, toUserId);
            
        } catch (Exception e) {
            log.error("推送私信消息失败: originalMessageId={}, fromUserId={}, toUserId={}, error={}", 
                    originalMessageId, fromUserId, toUserId, e.getMessage(), e);
        }
    }
    
    @Override
    public void pushLikeNotification(Long toUserId, Long fromUserId, Long itemId, String itemType) {
        UserPO fromUser = userMapper.selectById(fromUserId);
        String fromUserNickname = fromUser != null ? fromUser.getNickname() : "某用户";
        
        String title = "收到新的点赞";
        String content = String.format("%s 点赞了你的%s", fromUserNickname, getContentTypeName(itemType));
        
        pushNotification(toUserId, "LIKE", title, content, itemId, itemType, fromUserId);
    }
    
    @Override
    public void pushFavoriteNotification(Long toUserId, Long fromUserId, Long itemId, String itemType) {
        UserPO fromUser = userMapper.selectById(fromUserId);
        String fromUserNickname = fromUser != null ? fromUser.getNickname() : "某用户";
        
        String title = "收到新的收藏";
        String content = String.format("%s 收藏了你的%s", fromUserNickname, getContentTypeName(itemType));
        
        pushNotification(toUserId, "FAVORITE", title, content, itemId, itemType, fromUserId);
    }
    
    @Override
    public void pushCommentNotification(Long toUserId, Long fromUserId, Long itemId, 
                                       String itemType, String commentContent) {
        UserPO fromUser = userMapper.selectById(fromUserId);
        String fromUserNickname = fromUser != null ? fromUser.getNickname() : "某用户";
        
        String title = "收到新的评论";
        String content = String.format("%s 评论了你的%s: %s", 
                fromUserNickname, getContentTypeName(itemType), 
                truncateContent(commentContent, 50));
        
        pushNotification(toUserId, "COMMENT", title, content, itemId, itemType, fromUserId);
    }
    
    @Override
    public void pushShareNotification(Long toUserId, Long fromUserId, Long itemId, String itemType) {
        UserPO fromUser = userMapper.selectById(fromUserId);
        String fromUserNickname = fromUser != null ? fromUser.getNickname() : "某用户";
        
        String title = "收到新的转发";
        String content = String.format("%s 转发了你的%s", fromUserNickname, getContentTypeName(itemType));
        
        pushNotification(toUserId, "SHARE", title, content, itemId, itemType, fromUserId);
    }
    
    @Override
    public void pushSystemNotification(Long userId, String title, String content) {
        pushNotification(userId, "SYSTEM", title, content);
    }
    
    /**
     * 获取内容类型中文名称
     */
    private String getContentTypeName(String itemType) {
        switch (itemType.toUpperCase()) {
            case "POST":
                return "动态";
            case "COMMENT":
                return "评论";
            case "TASK":
                return "任务";
            default:
                return "内容";
        }
    }
    
    /**
     * 截断内容
     */
    private String truncateContent(String content, int maxLength) {
        if (content == null || content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }
}
