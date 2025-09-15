package com.xiaoyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyu.entity.FavoritePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavMapper extends BaseMapper<FavoritePO> {
}
