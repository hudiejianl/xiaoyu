package com.xiaoyua.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyua.dto.message.MessageCreateDTO;
import com.xiaoyua.vo.message.MessageVO;

/**
 * 私信消息服务接口
 * 
 * @author xiaoyu
 */
public interface MessageService {
    
    /**
     * 发送私信
     * 
     * @param fromUserId 发送者ID
     * @param messageDTO 消息DTO
     * @return 消息VO
     */
    MessageVO sendMessage(Long fromUserId, MessageCreateDTO messageDTO);
    
    /**
     * 获取聊天记录
     * 
     * @param userId 当前用户ID
     * @param friendId 好友ID
     * @param page 页码
     * @param size 每页数量
     * @return 聊天记录分页
     */
    IPage<MessageVO> getChatHistory(Long userId, Long friendId, Integer page, Integer size);
    
    /**
     * 标记消息为已读
     * 
     * @param userId 当前用户ID
     * @param fromUserId 发送者ID
     * @return 标记数量
     */
    int markMessagesAsRead(Long userId, Long fromUserId);
    
    /**
     * 获取未读消息数量
     * 
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Long getUnreadMessageCount(Long userId);
    
    /**
     * 检查是否为好友关系
     * 
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 是否为好友
     */
    boolean isFriend(Long userId1, Long userId2);
}
