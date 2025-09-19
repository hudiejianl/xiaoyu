package com.xiaoyua.service;

import com.xiaoyua.dto.post.PostCreateDTO;
import com.xiaoyua.dto.post.PostQueryDTO;
import com.xiaoyua.dto.post.PostUpdateDTO;
import com.xiaoyua.vo.post.PostVO;
import com.xiaoyua.vo.common.PageResult;
import java.util.List;

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

    /**
     * 创建动态（发布）
     * 说明：文件应当已通过 /files/upload 上传并返回 fileIds，
     * 该方法根据 dto 中的 fileIds 建立 posts 与 post_files 关联。
     *
     * @param postCreateDTO 创建参数
     * @param userId 当前用户ID
     * @return 创建完成的 PostVO（可返回概要信息）
     */
    PostVO createPost(PostCreateDTO postCreateDTO, Long userId);

    /**
     * 获取全部动态列表（已发布，按可见范围过滤）
     */
    PageResult<PostVO> listAll(Integer page, Integer size, String sort);

    /**
     * 获取指定用户的动态列表（已发布，按可见范围过滤）
     */
    PageResult<PostVO> listByUser(Long userId, Integer page, Integer size, String sort);

    /**
     * 获取热门动态（按点赞数倒序，默认前10条）
     */
    List<PostVO> listHot(Integer limit);
}

