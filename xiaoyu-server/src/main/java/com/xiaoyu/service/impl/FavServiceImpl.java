package com.xiaoyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoyu.entity.FavoritePO;
import com.xiaoyu.entity.NotificationPO;
import com.xiaoyu.entity.PostPO;
import com.xiaoyu.entity.UserPO;
import com.xiaoyu.mapper.FavMapper;
import com.xiaoyu.mapper.PostMapper;
import com.xiaoyu.mapper.UserMapper;
import com.xiaoyu.service.FavService;
import com.xiaoyu.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDateTime;

@Service
@Slf4j
public class FavServiceImpl implements FavService {
    @Autowired
    private FavMapper favMapper;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public void addFavorite(Long itemId, Long userId, String itemType) {
        // 检查是否已经收藏，避免重复收藏
        if (isFavorited(itemId, userId, itemType)) {
            return; // 已经收藏，直接返回
        }
        
        try {
            FavoritePO favorite = new FavoritePO();
            favorite.setUserId(userId);
            favorite.setItemId(itemId);
            favorite.setItemType(FavoritePO.ItemType.valueOf(itemType.toUpperCase()));
            favorite.setCreatedAt(LocalDateTime.now());
            favMapper.insert(favorite);
            
            // 创建收藏通知
            createFavoriteNotification(itemId, userId, itemType);
            
        } catch (DuplicateKeyException e) {
            // 处理并发情况下的重复插入
            // 由于有唯一约束，重复插入会抛出异常，这里忽略即可
        }
    }

    @Override
    public void deleteFavorite(Long itemId, Long userId, String itemType) {
        favMapper.delete(
                new QueryWrapper<FavoritePO>()
                        .eq("user_id", userId)
                        .eq("item_id", itemId)
                        .eq("item_type", itemType.toUpperCase())
        );
    }

    @Override
    public boolean isFaved(Long itemId, Long userId, String itemType) {
        Long count = favMapper.selectCount(
                new QueryWrapper<FavoritePO>()
                        .eq("user_id", userId)
                        .eq("item_id", itemId)
                        .eq("item_type", itemType.toUpperCase())
        );
        return count > 0;
    }

    @Override
    public boolean isFavorited(Long itemId, Long userId, String itemType) {
        Long count = favMapper.selectCount(
                new QueryWrapper<FavoritePO>()
                        .eq("user_id", userId)
                        .eq("item_id", itemId)
                        .eq("item_type", itemType.toUpperCase())
        );
        return count > 0;
    }

    @Override
    public long getFavoriteCount(Long itemId, String itemType) {
        return favMapper.selectCount(
                new QueryWrapper<FavoritePO>()
                        .eq("item_id", itemId)
                        .eq("item_type", itemType.toUpperCase())
        );
    }
    
    /**
     * 创建收藏通知
     */
    private void createFavoriteNotification(Long itemId, Long fromUserId, String itemType) {
        try {
            // 获取被收藏内容的作者ID
            Long toUserId = getContentAuthorId(itemId, itemType);
            
            // 不给自己发通知
            if (toUserId == null || toUserId.equals(fromUserId)) {
                return;
            }
            
            // 获取收藏用户信息
            UserPO fromUser = userMapper.selectById(fromUserId);
            if (fromUser == null) {
                return;
            }
            
            // 构建通知内容
            String title = "收到新的收藏";
            String content = String.format("%s 收藏了你的%s", 
                fromUser.getNickname(), 
                getContentTypeName(itemType));
            
            // 创建通知
            NotificationPO notification = new NotificationPO();
            notification.setUserId(toUserId);
            notification.setType(NotificationPO.Type.FAVORITE);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setRefId(itemId);
            notification.setRefType(getNotificationRefType(itemType));
            notification.setStatus(NotificationPO.Status.UNREAD);
            notification.setCreatedAt(LocalDateTime.now());
            
            notificationService.createNotification(notification);
            
        } catch (Exception e) {
            log.error("创建收藏通知失败: itemId={}, fromUserId={}, itemType={}, error={}", 
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
            case "TASK":
                // TODO: 如果需要支持任务收藏通知，需要添加TaskMapper
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
            case "TASK":
                return NotificationPO.RefType.TASK;
            default:
                return NotificationPO.RefType.POST;
        }
    }
}
