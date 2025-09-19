package com.xiaoyua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoyua.entity.FavoritePO;
import com.xiaoyua.entity.PostPO;
import com.xiaoyua.mapper.FavMapper;
import com.xiaoyua.mapper.PostMapper;
import com.xiaoyua.mapper.PostStatMapper;
import com.xiaoyua.service.FavService;
import com.xiaoyua.service.PushService;
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
    private PostMapper postMapper;
    
    @Autowired
    private PushService pushService;
    
    @Autowired
    private PostStatMapper postStatMapper;

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
            
            // 通过新的推送服务发送收藏通知
            createFavoriteNotificationViaPushService(itemId, userId, itemType);
            
            // 更新统计：仅对动态收藏计数
            if ("POST".equalsIgnoreCase(itemType)) {
                postStatMapper.incFav(itemId);
            }
            
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
        // 更新统计：仅对动态收藏-1
        if ("POST".equalsIgnoreCase(itemType)) {
            postStatMapper.decFav(itemId);
        }
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
     * 通过新的推送服务创建收藏通知
     */
    private void createFavoriteNotificationViaPushService(Long itemId, Long fromUserId, String itemType) {
        try {
            // 获取被收藏内容的作者ID
            Long toUserId = getContentAuthorId(itemId, itemType);
            
            // 不给自己发通知
            if (toUserId == null || toUserId.equals(fromUserId)) {
                return;
            }
            
            // 通过推送服务发送收藏通知
            pushService.pushFavoriteNotification(toUserId, fromUserId, itemId, itemType);
            
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
    
}
