package com.xiaoyua.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyua.entity.NotificationPO;
import com.xiaoyua.vo.notification.NotificationVO;

/**
 * 通知服务接口
 * 
 * @author xiaoyu
 */
public interface NotificationService {

    /**
     * 获取通知列表
     * 
     * @param userId 用户ID
     * @param type 通知类型
     * @param status 阅读状态
     * @param page 页码
     * @param size 每页数量
     * @return 通知列表
     */
    IPage<NotificationVO> getNotifications(Long userId, String type, String status, Integer page, Integer size);

    /**
     * 标记通知为已读
     * 
     * @param userId 用户ID
     * @param notificationId 通知ID
     * @return 是否成功
     */
    boolean markAsRead(Long userId, Long notificationId);

    /**
     * 标记所有通知为已读
     * 
     * @param userId 用户ID
     * @return 已标记数量
     */
    int markAllAsRead(Long userId);

    /**
     * 获取未读通知数量
     * 
     * @param userId 用户ID
     * @return 未读通知数量
     */
    long getUnreadCount(Long userId);

    /**
     * 创建通知
     * 
     * @param notification 通知对象
     * @return 是否成功
     */
    boolean createNotification(NotificationPO notification);

    /**
     * 转换PO为VO
     * 
     * @param notification 通知PO
     * @return 通知VO
     */
    NotificationVO convertToVO(NotificationPO notification);
}
