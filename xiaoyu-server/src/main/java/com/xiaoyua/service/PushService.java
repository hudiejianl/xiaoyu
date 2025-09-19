package com.xiaoyua.service;

/**
 * 独立推送服务接口
 * 负责统一处理所有推送逻辑，通过MQ实现解耦和可靠性
 * 
 * @author xiaoyu
 */
public interface PushService {
    
    /**
     * 推送通知消息
     * 
     * @param userId 用户ID
     * @param type 通知类型
     * @param title 通知标题
     * @param content 通知内容
     * @param refId 关联业务ID
     * @param refType 关联业务类型
     * @param fromUserId 发送者ID
     */
    void pushNotification(Long userId, String type, String title, String content, 
                         Long refId, String refType, Long fromUserId);
    
    /**
     * 推送通知消息（简化版）
     * 
     * @param userId 用户ID
     * @param type 通知类型
     * @param title 通知标题
     * @param content 通知内容
     */
    void pushNotification(Long userId, String type, String title, String content);
    
    /**
     * 推送私信消息
     * 
     * @param originalMessageId 原始消息ID
     * @param fromUserId 发送者ID
     * @param toUserId 接收者ID
     * @param content 消息内容
     * @param messageType 消息类型
     */
    void pushPrivateMessage(Long originalMessageId, Long fromUserId, Long toUserId, 
                           String content, String messageType);
    
    /**
     * 推送点赞通知
     * 
     * @param toUserId 被点赞者ID
     * @param fromUserId 点赞者ID
     * @param itemId 被点赞内容ID
     * @param itemType 被点赞内容类型
     */
    void pushLikeNotification(Long toUserId, Long fromUserId, Long itemId, String itemType);
    
    /**
     * 推送收藏通知
     * 
     * @param toUserId 被收藏者ID
     * @param fromUserId 收藏者ID
     * @param itemId 被收藏内容ID
     * @param itemType 被收藏内容类型
     */
    void pushFavoriteNotification(Long toUserId, Long fromUserId, Long itemId, String itemType);
    
    /**
     * 推送评论通知
     * 
     * @param toUserId 被评论者ID
     * @param fromUserId 评论者ID
     * @param itemId 被评论内容ID
     * @param itemType 被评论内容类型
     * @param commentContent 评论内容
     */
    void pushCommentNotification(Long toUserId, Long fromUserId, Long itemId, 
                                String itemType, String commentContent);
    
    /**
     * 推送转发通知
     * 
     * @param toUserId 被转发者ID
     * @param fromUserId 转发者ID
     * @param itemId 被转发内容ID
     * @param itemType 被转发内容类型
     */
    void pushShareNotification(Long toUserId, Long fromUserId, Long itemId, String itemType);
    
    /**
     * 推送系统通知
     * 
     * @param userId 用户ID
     * @param title 通知标题
     * @param content 通知内容
     */
    void pushSystemNotification(Long userId, String title, String content);
}
