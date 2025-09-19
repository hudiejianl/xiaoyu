package com.xiaoyua.service;

import com.xiaoyua.common.enums.TargetType;

public interface FavService {
    /**
     * 添加收藏
     * @param itemId 业务对象ID
     * @param userId 用户ID
     * @param itemType 业务类型
     */
    void addFavorite(Long itemId, Long userId, String itemType);
    
    /**
     * 添加收藏（枚举重载）
     */
    default void addFavorite(Long itemId, Long userId, TargetType itemType) {
        addFavorite(itemId, userId, itemType.value());
    }

     boolean isFaved(Long itemId, Long userId, String itemType);
    
    default boolean isFaved(Long itemId, Long userId, TargetType itemType) {
        return isFaved(itemId, userId, itemType.value());
    }

    /**
     * 取消收藏
     * @param itemId 业务对象ID
     * @param userId 用户ID
     * @param itemType 业务类型
     */
    void deleteFavorite(Long itemId, Long userId, String itemType);
    
    /**
     * 取消收藏（枚举重载）
     */
    default void deleteFavorite(Long itemId, Long userId, TargetType itemType) {
        deleteFavorite(itemId, userId, itemType.value());
    }
    
    /**
     * 检查用户是否已收藏
     * @param itemId 业务对象ID
     * @param userId 用户ID
     * @param itemType 业务类型
     * @return 是否已收藏
     */
    boolean isFavorited(Long itemId, Long userId, String itemType);
    
    /**
     * 检查用户是否已收藏（枚举重载）
     */
    default boolean isFavorited(Long itemId, Long userId, TargetType itemType) {
        return isFavorited(itemId, userId, itemType.value());
    }
    
    /**
     * 获取收藏数量
     * @param itemId 业务对象ID
     * @param itemType 业务类型
     * @return 收藏数量
     */
    long getFavoriteCount(Long itemId, String itemType);
    
    /**
     * 获取收藏数量（枚举重载）
     */
    default long getFavoriteCount(Long itemId, TargetType itemType) {
        return getFavoriteCount(itemId, itemType.value());
    }
    
    // 保持向后兼容的方法
    default void addFavorite(Long postId, Long userId) {
        addFavorite(postId, userId, "POST");
    }
    
    default void deleteFavorite(Long postId, Long userId) {
        deleteFavorite(postId, userId, "POST");
    }
}
