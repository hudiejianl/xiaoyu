package com.xiaoyua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoyua.entity.LikePO;
import com.xiaoyua.entity.PostPO;
import com.xiaoyua.mapper.LikeMapper;
import com.xiaoyua.mapper.PostMapper;
import com.xiaoyua.mapper.PostStatMapper;
import com.xiaoyua.service.LikeService;
import com.xiaoyua.service.PushService;
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
    private PostMapper postMapper;
    
    @Autowired
    private PushService pushService;
    
    @Autowired
    private PostStatMapper postStatMapper;

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
            
            // 通过新的推送服务发送点赞通知
            createLikeNotificationViaPushService(itemId, userId, itemType);
            
            // 更新统计：仅对动态点赞计数
            if ("POST".equalsIgnoreCase(itemType)) {
                postStatMapper.incLike(itemId);
            }
            
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
        // 更新统计：仅对动态取消点赞计数
        if ("POST".equalsIgnoreCase(itemType)) {
            postStatMapper.decLike(itemId);
        }
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
     * 通过新的推送服务创建点赞通知
     */
    private void createLikeNotificationViaPushService(Long itemId, Long fromUserId, String itemType) {
        try {
            // 获取被点赞内容的作者ID
            Long toUserId = getContentAuthorId(itemId, itemType);
            
            // 不给自己发通知
            if (toUserId == null || toUserId.equals(fromUserId)) {
                return;
            }
            
            // 通过推送服务发送点赞通知
            pushService.pushLikeNotification(toUserId, fromUserId, itemId, itemType);
            
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
    
}
