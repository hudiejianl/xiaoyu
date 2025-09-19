package com.xiaoyua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoyua.entity.MessagePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 私信消息Mapper接口
 * 
 * @author xiaoyu
 */
@Mapper
public interface MessageMapper extends BaseMapper<MessagePO> {
    
    /**
     * 分页查询两个用户之间的聊天记录
     * 
     * @param page 分页对象
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 聊天记录分页
     */
    IPage<MessagePO> selectChatHistory(Page<MessagePO> page, 
                                      @Param("userId1") Long userId1, 
                                      @Param("userId2") Long userId2);
    
    /**
     * 标记消息为已读
     * 
     * @param fromId 发送者ID
     * @param toId 接收者ID
     * @return 更新条数
     */
    int markMessagesAsRead(@Param("fromId") Long fromId, @Param("toId") Long toId);
    
    /**
     * 获取未读消息数量
     * 
     * @param toId 接收者ID
     * @return 未读消息数量
     */
    Long getUnreadCount(@Param("toId") Long toId);
}
