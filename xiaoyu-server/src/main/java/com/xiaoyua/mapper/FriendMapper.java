package com.xiaoyua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyua.entity.FriendPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 好友关系Mapper接口
 * 
 * @author xiaoyu
 */
@Mapper
public interface FriendMapper extends BaseMapper<FriendPO> {
}
