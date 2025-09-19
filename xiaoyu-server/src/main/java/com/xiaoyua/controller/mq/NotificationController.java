package com.xiaoyua.controller.mq;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyua.context.BaseContext;
import com.xiaoyua.result.Result;
import com.xiaoyua.service.NotificationService;
import com.xiaoyua.vo.notification.NotificationVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知中心控制器
 * 
 * @author xiaoyu
 */
@RestController
@RequestMapping("/notifications")
@Slf4j
@Tag(name = "通知中心", description = "通知中心相关接口")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    @Operation(summary = "获取通知列表", description = "获取当前登录用户的通知列表")
    public Result<IPage<NotificationVO>> getNotifications(
            @Parameter(description = "通知类型：LIKE/FAVORITE/COMMENT/SHARE/TASK_ORDER/SYSTEM/VIOLATION") 
            @RequestParam(required = false) String type,
            @Parameter(description = "阅读状态：UNREAD/read，默认UNREAD") 
            @RequestParam(required = false) String status,
            @Parameter(description = "页码，默认1") 
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量，默认20") 
            @RequestParam(defaultValue = "20") Integer size) {
        
        Long userId = BaseContext.getCurrentId();
        log.info("获取通知列表: userId={}, type={}, status={}, page={}, size={}", 
                userId, type, status, page, size);
        
        IPage<NotificationVO> notifications = notificationService.getNotifications(userId, type, status, page, size);
        return Result.success(notifications);
    }

    @PutMapping("/{notification_id}/read")
    @Operation(summary = "标记通知为已读", description = "标记单条通知为已读")
    public Result<Void> markAsRead(
            @Parameter(description = "通知ID") 
            @PathVariable("notification_id") Long notificationId) {
        
        Long userId = BaseContext.getCurrentId();
        log.info("标记通知为已读: userId={}, notificationId={}", userId, notificationId);
        
        boolean success = notificationService.markAsRead(userId, notificationId);
        
        if (success) {
            return Result.success("通知已标记为已读");
        } else {
            return Result.error("标记失败，通知不存在或已读");
        }
    }

    @PutMapping("/read_all")
    @Operation(summary = "标记所有通知为已读", description = "标记所有未读通知为已读")
    public Result<Void> markAllAsRead() {
        Long userId = BaseContext.getCurrentId();
        log.info("标记所有通知为已读: userId={}", userId);
        
        int count = notificationService.markAllAsRead(userId);
        
        return Result.success("所有通知已标记为已读");
    }

    @GetMapping("/unread_count")
    @Operation(summary = "获取未读通知数量", description = "获取当前登录用户的未读通知数量")
    public Result<Map<String, Object>> getUnreadCount() {
        Long userId = BaseContext.getCurrentId();
        log.info("获取未读通知数量: userId={}", userId);
        
        long count = notificationService.getUnreadCount(userId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("count", count);
        
        return Result.success(data);
    }
}
