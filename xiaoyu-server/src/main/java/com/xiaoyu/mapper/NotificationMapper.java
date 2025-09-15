package com.xiaoyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyu.entity.NotificationPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知数据访问层
 * 
 * @author xiaoyu
 */
@Mapper
public interface NotificationMapper extends BaseMapper<NotificationPO> {
}
