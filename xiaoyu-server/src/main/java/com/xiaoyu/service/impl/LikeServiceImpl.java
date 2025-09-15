package com.xiaoyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoyu.entity.LikePO;
import com.xiaoyu.entity.NotificationPO;
import com.xiaoyu.entity.PostPO;
import com.xiaoyu.entity.UserPO;
import com.xiaoyu.mapper.LikeMapper;
import com.xiaoyu.mapper.PostMapper;
import com.xiaoyu.mapper.UserMapper;
import com.xiaoyu.service.LikeService;
import com.xiaoyu.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public void addLike(Long itemId, Long userId, String itemType) {
        // 检查是否已经点赞，避免重复点赞
        if (isLiked(itemId, userId, itemType)) {
            return; // 已经点赞，直接返回
        }
        
        try {
            LikePO like = new LikePO();
            like.setUserId(userId);
            like.setItemId(itemId);
            like.setItemType(LikePO.ItemType.valueOf(itemType.toUpperCase()));
            like.setCreatedAt(LocalDateTime.now());
            likeMapper.insert(like);
            
            // 创建点赞通知
            createLikeNotification(itemId, userId, itemType);
            
        } catch (DuplicateKeyException e) {
            // 处理并发情况下的重复插入
            // 由于有唯一约束，重复插入会抛出异常，这里忽略即可
        }
    }

    @Override
    public void deleteLike(Long itemId, Long userId, String itemType) {
        likeMapper.delete(
                new QueryWrapper<LikePO>()
                        .eq("user_id", userId)
                        .eq("item_id", itemId)
                        .eq("item_type", itemType.toUpperCase())
        );
    }

    @Override
    public boolean isLiked(Long itemId, Long userId, String itemType) {
        Long count = likeMapper.selectCount(
                new QueryWrapper<LikePO>()
                        .eq("user_id", userId)
                        .eq("item_id", itemId)
                        .eq("item_type", itemType.toUpperCase())
        );
        return count > 0;
    }

    @Override
    public long getLikeCount(Long itemId, String itemType) {
        return likeMapper.selectCount(
                new QueryWrapper<LikePO>()
                        .eq("item_id", itemId)
                        .eq("item_type", itemType.toUpperCase())
        );
    }
    
    /**
     * 创建点赞通知
     */
    private void createLikeNotification(Long itemId, Long fromUserId, String itemType) {
        try {
            // 获取被点赞内容的作者ID
            Long toUserId = getContentAuthorId(itemId, itemType);
            
            // 不给自己发通知
            if (toUserId == null || toUserId.equals(fromUserId)) {
                return;
            }
            
            // 获取点赞用户信息
            UserPO fromUser = userMapper.selectById(fromUserId);
            if (fromUser == null) {
                return;
            }
            
            // 构建通知内容
            String title = "收到新的点赞";
            String content = String.format("%s 点赞了你的%s", 
                fromUser.getNickname(), 
                getContentTypeName(itemType));
            
            // 创建通知
            NotificationPO notification = new NotificationPO();
            notification.setUserId(toUserId);
            notification.setType(NotificationPO.Type.LIKE);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setRefId(itemId);
            notification.setRefType(getNotificationRefType(itemType));
            notification.setStatus(NotificationPO.Status.UNREAD);
            notification.setCreatedAt(LocalDateTime.now());
            
            notificationService.createNotification(notification);
            
        } catch (Exception e) {
            log.error("创建点赞通知失败: itemId={}, fromUserId={}, itemType={}, error={}", 
                itemId, fromUserId, itemType, e.getMessage(), e);
        }
    }
    
    /**
     * 获取内容作者ID
     */
    private Long getContentAuthorId(Long itemId, String itemType) {
        switch (itemType.toUpperCase()) {
            case "POST":
                PostPO post = postMapper.selectById(itemId);
                return post != null ? post.getUserId() : null;
            case "COMMENT":
                // TODO: 如果需要支持评论点赞通知，需要添加CommentMapper
                return null;
            default:
                return null;
        }
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
     * 获取通知关联类型
     */
    private NotificationPO.RefType getNotificationRefType(String itemType) {
        switch (itemType.toUpperCase()) {
            case "POST":
                return NotificationPO.RefType.POST;
            case "COMMENT":
                return NotificationPO.RefType.COMMENT;
            case "TASK":
                return NotificationPO.RefType.TASK;
            default:
                return NotificationPO.RefType.POST;
        }
    }
}
