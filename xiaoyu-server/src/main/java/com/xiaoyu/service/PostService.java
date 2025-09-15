package com.xiaoyu.service;

import com.xiaoyu.dto.post.PostQueryDTO;
import com.xiaoyu.dto.post.PostUpdateDTO;
import com.xiaoyu.vo.post.PostVO;
import com.xiaoyu.vo.common.PageResult;

public interface PostService {
    /**
     * 获取动态列表
     * @param postQueryDTO 查询条件
     * @return 分页结果
     */
    PageResult<PostVO> getPosts(PostQueryDTO postQueryDTO);
    
    /**
     * 获取动态详情
     * @param postId 动态ID
     * @return 动态详情
     */
    PostVO getPostDetail(Long postId);

    /**
     * 更新动态
     * @param postUpdateDTO
     * @param postId
     */
    void updatePost(PostUpdateDTO postUpdateDTO,Long postId);
}
