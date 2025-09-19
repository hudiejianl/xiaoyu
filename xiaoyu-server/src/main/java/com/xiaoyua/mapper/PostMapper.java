package com.xiaoyua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyua.entity.PostPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<PostPO>{
    /**
     * 查询热门动态（按点赞数倒序）
     * 说明：依赖 post_stats.like_cnt 字段
     */
    @Select("SELECT p.* FROM posts p " +
            "LEFT JOIN post_stats s ON s.post_id = p.id " +
            "WHERE p.status = 'PUBLISHED' AND p.visibility = 'PUBLIC' " +
            "ORDER BY s.view_cnt DESC, p.created_at DESC " +
            "LIMIT #{limit}")
    List<PostPO> selectHotPosts(@Param("limit") int limit);
}
