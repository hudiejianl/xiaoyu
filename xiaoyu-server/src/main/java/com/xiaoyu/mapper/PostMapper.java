package com.xiaoyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyu.entity.LikePO;
import com.xiaoyu.entity.PostPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<PostPO>{

}
