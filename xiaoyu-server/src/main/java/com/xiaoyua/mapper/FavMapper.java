package com.xiaoyua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyua.entity.FavoritePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavMapper extends BaseMapper<FavoritePO> {
}
