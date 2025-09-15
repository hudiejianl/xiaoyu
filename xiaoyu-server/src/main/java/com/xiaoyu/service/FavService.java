package com.xiaoyu.service;

public interface FavService {
    /**
     * 添加收藏
     * @param itemId 业务对象ID
     * @param userId 用户ID
     * @param itemType 业务类型
     */
    void addFavorite(Long itemId, Long userId, String itemType);

     boolean isFaved(Long itemId, Long userId, String itemType);

    /**
     * 取消收藏
     * @param itemId 业务对象ID
     * @param userId 用户ID
     * @param itemType 业务类型
     */
    void deleteFavorite(Long itemId, Long userId, String itemType);
    
    /**
     * 检查用户是否已收藏
     * @param itemId 业务对象ID
     * @param userId 用户ID
     * @param itemType 业务类型
     * @return 是否已收藏
     */
    boolean isFavorited(Long itemId, Long userId, String itemType);
    
    /**
     * 获取收藏数量
     * @param itemId 业务对象ID
     * @param itemType 业务类型
     * @return 收藏数量
     */
    long getFavoriteCount(Long itemId, String itemType);
    
    // 保持向后兼容的方法
    default void addFavorite(Long postId, Long userId) {
        addFavorite(postId, userId, "POST");
    }
    
    default void deleteFavorite(Long postId, Long userId) {
        deleteFavorite(postId, userId, "POST");
    }
}
