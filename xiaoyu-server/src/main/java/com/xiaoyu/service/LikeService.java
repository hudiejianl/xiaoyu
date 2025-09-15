package com.xiaoyu.service;

import com.xiaoyu.entity.LikePO;

public interface LikeService {
    /**
     * 添加点赞
     * @param itemId 业务对象ID
     * @param userId 用户ID
     * @param itemType 业务类型
     */
    void addLike(Long itemId, Long userId, String itemType);
    
    /**
     * 取消点赞
     * @param itemId 业务对象ID
     * @param userId 用户ID
     * @param itemType 业务类型
     */
    void deleteLike(Long itemId, Long userId, String itemType);
    
    /**
     * 检查用户是否已点赞
     * @param itemId 业务对象ID
     * @param userId 用户ID
     * @param itemType 业务类型
     * @return 是否已点赞
     */
    boolean isLiked(Long itemId, Long userId, String itemType);
    
    /**
     * 获取点赞数量
     * @param itemId 业务对象ID
     * @param itemType 业务类型
     * @return 点赞数量
     */
    long getLikeCount(Long itemId, String itemType);
}
