package com.xiaoyua.controller.mq;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyua.context.BaseContext;
import com.xiaoyua.dto.message.MessageCreateDTO;
import com.xiaoyua.result.Result;
import com.xiaoyua.service.MessageService;
import com.xiaoyua.vo.message.MessageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 私信消息控制器
 * 
 * @deprecated 推荐使用WebSocket直接发送私信消息以获得更好的实时性
 * HTTP接口仍然保留用于兼容性和特殊场景
 * 
 * @author xiaoyu
 */
@RestController
@RequestMapping("/api/friends/messages")
@Slf4j
@Tag(name = "私信管理", description = "好友私信相关接口")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @PostMapping
    @Operation(summary = "发送私信", description = "向好友发送私信")
    @Deprecated
    public Result<MessageVO> sendMessage(@Valid @RequestBody MessageCreateDTO messageDTO) {
        Long fromUserId = BaseContext.getCurrentId();
        log.info("发送私信: fromUserId={}, toId={}, content={}", fromUserId, messageDTO.getToId(), messageDTO.getContent());
        
        MessageVO messageVO = messageService.sendMessage(fromUserId, messageDTO);
        return Result.success(messageVO);
    }
    
    @GetMapping
    @Operation(summary = "获取聊天记录", description = "获取与好友的聊天记录")
    public Result<IPage<MessageVO>> getChatHistory(
            @Parameter(description = "好友ID") @RequestParam("friend_id") Long friendId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "20") Integer size) {
        
        Long userId = BaseContext.getCurrentId();
        log.info("获取聊天记录: userId={}, friendId={}, page={}, size={}", userId, friendId, page, size);
        
        IPage<MessageVO> chatHistory = messageService.getChatHistory(userId, friendId, page, size);
        return Result.success(chatHistory);
    }
    
    @PutMapping("/read")
    @Operation(summary = "标记消息已读", description = "标记来自指定用户的消息为已读")
    public Result<Void> markMessagesAsRead(
            @Parameter(description = "发送者ID") @RequestParam("from_id") Long fromId) {
        
        Long userId = BaseContext.getCurrentId();
        log.info("标记消息已读: userId={}, fromId={}", userId, fromId);
        
        int count = messageService.markMessagesAsRead(userId, fromId);
        log.info("标记了 {} 条消息为已读", count);
        
        return Result.success();
    }
    
    @GetMapping("/unread-count")
    @Operation(summary = "获取未读消息数量", description = "获取当前用户的未读私信数量")
    public Result<Long> getUnreadMessageCount() {
        Long userId = BaseContext.getCurrentId();
        log.info("获取未读消息数量: userId={}", userId);
        
        Long unreadCount = messageService.getUnreadMessageCount(userId);
        return Result.success(unreadCount);
    }
}
