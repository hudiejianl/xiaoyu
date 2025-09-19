package com.xiaoyua.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaoyua.entity.PostStatPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PostStatMapper extends BaseMapper<PostStatPO> {

    @Insert("INSERT INTO post_stats (post_id, view_cnt, like_cnt, fav_cnt, comment_cnt, share_cnt) VALUES (#{postId}, 0, 0, 0, 0, 0) " +
            "ON DUPLICATE KEY UPDATE post_id = post_id")
    int initIfAbsent(@Param("postId") Long postId);

    @Update("INSERT INTO post_stats (post_id, view_cnt, like_cnt, fav_cnt, comment_cnt, share_cnt) VALUES (#{postId}, 1, 0, 0, 0, 0) " +
            "ON DUPLICATE KEY UPDATE view_cnt = view_cnt + 1")
    int incView(@Param("postId") Long postId);

    @Update("INSERT INTO post_stats (post_id, view_cnt, like_cnt, fav_cnt, comment_cnt, share_cnt) VALUES (#{postId}, 0, 1, 0, 0, 0) " +
            "ON DUPLICATE KEY UPDATE like_cnt = like_cnt + 1")
    int incLike(@Param("postId") Long postId);

    @Update("UPDATE post_stats SET like_cnt = CASE WHEN like_cnt > 0 THEN like_cnt - 1 ELSE 0 END WHERE post_id = #{postId}")
    int decLike(@Param("postId") Long postId);

    @Update("INSERT INTO post_stats (post_id, view_cnt, like_cnt, fav_cnt, comment_cnt, share_cnt) VALUES (#{postId}, 0, 0, 0, 1, 0) " +
            "ON DUPLICATE KEY UPDATE comment_cnt = comment_cnt + 1")
    int incComment(@Param("postId") Long postId);

    @Update("UPDATE post_stats SET comment_cnt = CASE WHEN comment_cnt > 0 THEN comment_cnt - 1 ELSE 0 END WHERE post_id = #{postId}")
    int decComment(@Param("postId") Long postId);

    @Update("INSERT INTO post_stats (post_id, view_cnt, like_cnt, fav_cnt, comment_cnt, share_cnt) VALUES (#{postId}, 0, 0, 0, 0, 1) " +
            "ON DUPLICATE KEY UPDATE share_cnt = share_cnt + 1")
    int incShare(@Param("postId") Long postId);

    @Update("INSERT INTO post_stats (post_id, view_cnt, like_cnt, fav_cnt, comment_cnt, share_cnt) VALUES (#{postId}, 0, 0, 1, 0, 0) " +
            "ON DUPLICATE KEY UPDATE fav_cnt = fav_cnt + 1")
    int incFav(@Param("postId") Long postId);

    @Update("UPDATE post_stats SET fav_cnt = CASE WHEN fav_cnt > 0 THEN fav_cnt - 1 ELSE 0 END WHERE post_id = #{postId}")
    int decFav(@Param("postId") Long postId);
}
