package com.xiaoyua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyua.entity.OfflineMessagePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 离线消息Mapper
 * 
 * @author xiaoyu
 */
@Mapper
public interface OfflineMessageMapper extends BaseMapper<OfflineMessagePO> {
    
    /**
     * 获取用户的离线消息列表
     * 
     * @param userId 用户ID
     * @param status 消息状态
     * @param limit 限制数量
     * @return 离线消息列表
     */
    @Select("SELECT * FROM offline_messages WHERE user_id = #{userId} AND status = #{status} " +
            "AND deleted = 0 ORDER BY created_at ASC LIMIT #{limit}")
    List<OfflineMessagePO> selectOfflineMessages(@Param("userId") Long userId, 
                                                 @Param("status") String status, 
                                                 @Param("limit") Integer limit);
    
    /**
     * 批量更新消息状态
     * 
     * @param ids 消息ID列表
     * @param status 新状态
     * @param retryCount 重试次数
     * @return 更新数量
     */
    @Update("<script>" +
            "UPDATE offline_messages SET status = #{status}, retry_count = #{retryCount}, " +
            "updated_at = NOW() WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchUpdateStatus(@Param("ids") List<Long> ids, 
                         @Param("status") String status, 
                         @Param("retryCount") Integer retryCount);
    
    /**
     * 清理过期的离线消息
     * 
     * @param expireTime 过期时间
     * @return 清理数量
     */
    @Update("UPDATE offline_messages SET status = 'EXPIRED', updated_at = NOW() " +
            "WHERE expire_at < #{expireTime} AND status = 'PENDING' AND deleted = 0")
    int expireOldMessages(@Param("expireTime") LocalDateTime expireTime);
    
    /**
     * 获取用户未推送的离线消息数量
     * 
     * @param userId 用户ID
     * @return 未推送消息数量
     */
    @Select("SELECT COUNT(*) FROM offline_messages WHERE user_id = #{userId} " +
            "AND status = 'PENDING' AND deleted = 0")
    Long countPendingMessages(@Param("userId") Long userId);
}
