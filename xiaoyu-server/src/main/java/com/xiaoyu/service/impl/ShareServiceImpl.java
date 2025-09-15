package com.xiaoyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoyu.entity.NotificationPO;
import com.xiaoyu.entity.PostPO;
import com.xiaoyu.entity.SharePO;
import com.xiaoyu.entity.UserPO;
import com.xiaoyu.mapper.PostMapper;
import com.xiaoyu.mapper.ShareMapper;
import com.xiaoyu.mapper.UserMapper;
import com.xiaoyu.service.PushService;
import com.xiaoyu.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShareServiceImpl implements ShareService {
    @Autowired
    ShareMapper shareMapper;
    
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PushService pushService;
    
    @Override
    public void addShare(Long postId, Long userId) {
        SharePO share=new SharePO();
        share.setUserId(userId);
        share.setItemId(postId);
        share.setItemType(SharePO.ItemType.POST);
        share.setCreatedAt(LocalDateTime.now());
        shareMapper.insert(share);
        
        // 创建分享通知
        createShareNotification(postId, userId);
    }

    @Override
    public long getShareCount(Long postId,String type){
        return shareMapper.selectCount(
                new QueryWrapper<SharePO>()
                        .eq("item_id", postId)
                        .eq("item_type", type.toUpperCase())
        );
    }
    
    /**
     * 创建分享通知
     */
    private void createShareNotification(Long postId, Long fromUserId) {
        try {
            // 获取动态作者ID
            PostPO post = postMapper.selectById(postId);
            if (post == null) {
                return;
            }
            
            Long toUserId = post.getUserId();
            
            // 不给自己发通知
            if (toUserId.equals(fromUserId)) {
                return;
            }
            
            // 获取分享用户信息
            UserPO fromUser = userMapper.selectById(fromUserId);
            if (fromUser == null) {
                return;
            }
            
            // 构建通知内容
            String title = "动态被分享";
            String content = String.format("%s 分享了你的动态", fromUser.getNickname());
            
            // 使用PushService发送通知
            pushService.pushNotification(
                toUserId,
                NotificationPO.Type.SHARE.name(),
                title,
                content,
                postId,
                NotificationPO.RefType.POST.name(),
                fromUserId
            );
            
        } catch (Exception e) {
            // 使用简单的日志记录，避免引入额外依赖
            System.err.println("创建分享通知失败: postId=" + postId + ", fromUserId=" + fromUserId + ", error=" + e.getMessage());
        }
    }

}
