package com.xiaoyua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoyua.entity.NotificationPO;
import com.xiaoyua.entity.UserPO;
import com.xiaoyua.mapper.NotificationMapper;
import com.xiaoyua.mapper.UserMapper;
import com.xiaoyua.service.NotificationService;
import com.xiaoyua.vo.notification.NotificationVO;
import com.xiaoyua.vo.user.UserSimpleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * 通知服务实现类
 * 
 * @author xiaoyu
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Override
    public IPage<NotificationVO> getNotifications(Long userId, String type, String status, Integer page, Integer size) {
        log.info("获取通知列表: userId={}, type={}, status={}, page={}, size={}", userId, type, status, page, size);
        
        // 构建查询条件
        QueryWrapper<NotificationPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        
        // 按类型筛选
        if (StringUtils.hasText(type)) {
            try {
                NotificationPO.Type t = NotificationPO.Type.valueOf(type.trim().toUpperCase(Locale.ROOT));
                queryWrapper.eq("type", t);
            } catch (IllegalArgumentException ex) {
                log.warn("忽略非法通知类型筛选: {}", type);
            }
        }
        
        // 按状态筛选
        if (StringUtils.hasText(status)) {
            try {
                NotificationPO.Status s = NotificationPO.Status.valueOf(status.trim().toUpperCase(Locale.ROOT));
                queryWrapper.eq("status", s);
            } catch (IllegalArgumentException ex) {
                log.warn("忽略非法通知状态筛选: {}", status);
            }
        }
        
        // 按创建时间倒序排列
        queryWrapper.orderByDesc("created_at");
        
        // 分页查询
        int p = (page == null || page <= 0) ? 1 : page;
        int sz = (size == null || size <= 0 || size > 100) ? 20 : size; // 限制单页最大100
        Page<NotificationPO> pageParam = new Page<>(p, sz);
        IPage<NotificationPO> notificationPage = notificationMapper.selectPage(pageParam, queryWrapper);
        
        // 转换为VO
        return notificationPage.convert(this::convertToVO);
    }

    @Override
    public boolean markAsRead(Long userId, Long notificationId) {
        log.info("标记通知为已读: userId={}, notificationId={}", userId, notificationId);
        
        UpdateWrapper<NotificationPO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", notificationId)
                    .eq("user_id", userId)
                    .set("status", NotificationPO.Status.READ);
        
        int updated = notificationMapper.update(null, updateWrapper);
        boolean success = updated > 0;
        
        if (success) {
            log.info("通知标记已读成功: notificationId={}", notificationId);
            // TODO: 如果需要，接入 PushService 推送未读数更新事件
        } else {
            log.warn("通知标记已读失败: notificationId={}", notificationId);
        }
        
        return success;
    }

    @Override
    public int markAllAsRead(Long userId) {
        log.info("标记所有通知为已读: userId={}", userId);
        
        UpdateWrapper<NotificationPO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId)
                    .eq("status", NotificationPO.Status.UNREAD)
                    .set("status", NotificationPO.Status.READ);
        
        int updated = notificationMapper.update(null, updateWrapper);
        
        log.info("标记所有通知为已读完成: userId={}, updated={}", userId, updated);
        return updated;
    }

    @Override
    public long getUnreadCount(Long userId) {
        log.debug("获取未读通知数量: userId={}", userId);
        
        QueryWrapper<NotificationPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("status", NotificationPO.Status.UNREAD);
        
        long count = notificationMapper.selectCount(queryWrapper);
        log.debug("未读通知数量: userId={}, count={}", userId, count);
        
        return count;
    }

    @Override
    public boolean createNotification(NotificationPO notification) {
        log.info("创建通知: userId={}, type={}, title={}", notification.getUserId(), notification.getType(), notification.getTitle());
        
        // 设置默认值
        if (notification.getStatus() == null) {
            notification.setStatus(NotificationPO.Status.UNREAD);
        }
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(LocalDateTime.now());
        }
        
        int inserted = notificationMapper.insert(notification);
        boolean success = inserted > 0;
        
        return success;
    }

    @Override
    public NotificationVO convertToVO(NotificationPO notification) {
        if (notification == null) {
            return null;
        }
        
        NotificationVO vo = new NotificationVO();
        vo.setId(notification.getId());
        vo.setType(notification.getType().name());
        vo.setTitle(notification.getTitle());
        vo.setContent(notification.getContent());
        vo.setRefId(notification.getRefId());
        vo.setRefType(notification.getRefType() != null ? notification.getRefType().name() : null);
        vo.setStatus(notification.getStatus().name());
        vo.setCreatedAt(notification.getCreatedAt());
        vo.setCount(1); // 默认数量为1，聚合通知时可以修改
        
        // 根据通知类型和关联ID获取来源用户信息
        UserSimpleVO fromUser = getFromUserByNotification(notification);
        vo.setFromUser(fromUser);
        
        return vo;
    }

    /**
     * 根据通知信息获取来源用户
     *
     * @param notification 通知对象
     * @return 来源用户信息
     */
    private UserSimpleVO getFromUserByNotification(NotificationPO notification) {
        // 对于系统通知和违规通知，没有来源用户
        if (notification.getType() == NotificationPO.Type.SYSTEM ||
            notification.getType() == NotificationPO.Type.VIOLATION) {
            return null;
        }
        // TODO: 目前 NotificationPO 未包含 fromUserId 字段，无法直接解析来源用户。
        // 建议在通知表增加 from_user_id 字段，并在创建通知时写入，
        // 然后在此处通过 userMapper.selectById(fromUserId) 构建 UserSimpleVO。
        return null;
    }

    /**
     * 转换用户PO为简单VO
     * 
     * @param user 用户PO
     * @return 用户简单VO
     */
    private UserSimpleVO convertToUserSimpleVO(UserPO user) {
        if (user == null) {
            return null;
        }
        
        UserSimpleVO vo = new UserSimpleVO();
        vo.setId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setGender(user.getGender());
        vo.setCampusId(user.getCampusId());
        vo.setIsRealName(user.getIsRealName());
        vo.setCreatedAt(user.getCreatedAt());
        
        return vo;
    }
}
