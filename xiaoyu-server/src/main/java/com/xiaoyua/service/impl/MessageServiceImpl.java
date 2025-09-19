package com.xiaoyua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoyua.dto.message.MessageCreateDTO;
import com.xiaoyua.entity.FriendPO;
import com.xiaoyua.entity.MessagePO;
import com.xiaoyua.mapper.FriendMapper;
import com.xiaoyua.mapper.MessageMapper;
import com.xiaoyua.service.MessageService;
import com.xiaoyua.service.PushService;
import com.xiaoyua.vo.message.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 私信消息服务实现类
 * 
 * @author xiaoyu
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    
    @Autowired
    private MessageMapper messageMapper;
    
    @Autowired
    private FriendMapper friendMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private PushService pushService;
    
    private static final String UNREAD_COUNT_KEY = "message:unread:";
    private static final String FRIEND_CACHE_KEY = "friend:relation:";
    
    @Override
    @Transactional
    public MessageVO sendMessage(Long fromUserId, MessageCreateDTO messageDTO) {
        // 检查是否为好友关系
        if (!isFriend(fromUserId, messageDTO.getToId())) {
            throw new RuntimeException("只能向好友发送私信");
        }
        
        // 创建消息实体
        MessagePO messagePO = new MessagePO();
        messagePO.setFromId(fromUserId);
        messagePO.setToId(messageDTO.getToId());
        messagePO.setContent(messageDTO.getContent());
        messagePO.setMessageType(messageDTO.getMessageType());
        messagePO.setStatus("SENT");
        messagePO.setIsRead(0);
        messagePO.setCreatedAt(LocalDateTime.now());
        messagePO.setUpdatedAt(LocalDateTime.now());
        
        // 保存到数据库
        messageMapper.insert(messagePO);
        
        // 更新接收者未读消息数量缓存
        String unreadKey = UNREAD_COUNT_KEY + messageDTO.getToId();
        redisTemplate.opsForValue().increment(unreadKey);
        redisTemplate.expire(unreadKey, 7, TimeUnit.DAYS);
        
        // 通过MQ推送私信消息
        try {
            pushService.pushPrivateMessage(
                messagePO.getId(), 
                fromUserId, 
                messageDTO.getToId(), 
                messageDTO.getContent(), 
                messageDTO.getMessageType()
            );
        } catch (Exception e) {
            log.error("推送私信消息失败: messageId={}, fromUserId={}, toUserId={}, error={}", 
                    messagePO.getId(), fromUserId, messageDTO.getToId(), e.getMessage(), e);
        }
        
        // 转换为VO返回
        return convertToVO(messagePO);
    }
    
    @Override
    public IPage<MessageVO> getChatHistory(Long userId, Long friendId, Integer page, Integer size) {
        // 检查是否为好友关系
        if (!isFriend(userId, friendId)) {
            throw new RuntimeException("只能查看好友的聊天记录");
        }
        
        Page<MessagePO> pageObj = new Page<>(page, size);
        IPage<MessagePO> messagePage = messageMapper.selectChatHistory(pageObj, userId, friendId);
        
        // 转换为VO
        return messagePage.convert(this::convertToVO);
    }
    
    @Override
    @Transactional
    public int markMessagesAsRead(Long userId, Long fromUserId) {
        // 标记消息为已读
        int count = messageMapper.markMessagesAsRead(fromUserId, userId);
        
        if (count > 0) {
            // 更新Redis中的未读消息数量
            String unreadKey = UNREAD_COUNT_KEY + userId;
            Long currentCount = (Long) redisTemplate.opsForValue().get(unreadKey);
            if (currentCount != null && currentCount > count) {
                redisTemplate.opsForValue().set(unreadKey, currentCount - count);
            } else {
                redisTemplate.delete(unreadKey);
            }
        }
        
        return count;
    }
    
    @Override
    public Long getUnreadMessageCount(Long userId) {
        // 先从Redis缓存中获取
        String unreadKey = UNREAD_COUNT_KEY + userId;
        Long cachedCount = (Long) redisTemplate.opsForValue().get(unreadKey);
        
        if (cachedCount != null) {
            return cachedCount;
        }
        
        // 缓存中没有，从数据库查询
        Long dbCount = messageMapper.getUnreadCount(userId);
        if (dbCount == null) {
            dbCount = 0L;
        }
        
        // 更新缓存
        if (dbCount > 0) {
            redisTemplate.opsForValue().set(unreadKey, dbCount, 7, TimeUnit.DAYS);
        }
        
        return dbCount;
    }
    
    @Override
    public boolean isFriend(Long userId1, Long userId2) {
        // 先从Redis缓存中查询
        String cacheKey = FRIEND_CACHE_KEY + Math.min(userId1, userId2) + ":" + Math.max(userId1, userId2);
        Boolean cached = (Boolean) redisTemplate.opsForValue().get(cacheKey);
        
        if (cached != null) {
            return cached;
        }
        
        // 缓存中没有，从数据库查询
        QueryWrapper<FriendPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> 
            wrapper.eq("user_id", userId1).eq("friend_id", userId2)
                   .or()
                   .eq("user_id", userId2).eq("friend_id", userId1)
        );
        queryWrapper.eq("status", "ACCEPTED");
        
        long count = friendMapper.selectCount(queryWrapper);
        boolean isFriend = count > 0;
        
        // 更新缓存，缓存30分钟
        redisTemplate.opsForValue().set(cacheKey, isFriend, 30, TimeUnit.MINUTES);
        
        return isFriend;
    }
    
    /**
     * 转换PO为VO
     */
    private MessageVO convertToVO(MessagePO messagePO) {
        MessageVO messageVO = new MessageVO();
        BeanUtils.copyProperties(messagePO, messageVO);
        return messageVO;
    }
}
