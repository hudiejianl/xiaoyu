package com.xiaoyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoyu.context.BaseContext;
import com.xiaoyu.dto.post.PostQueryDTO;
import com.xiaoyu.dto.post.PostUpdateDTO;
import com.xiaoyu.entity.PostPO;
import com.xiaoyu.mapper.PostMapper;
import com.xiaoyu.service.LikeService;
import com.xiaoyu.service.PostService;
import com.xiaoyu.service.ShareService;
import com.xiaoyu.vo.post.PostVO;
import com.xiaoyu.vo.post.PostStatsVO;
import com.xiaoyu.vo.post.PostUserActionsVO;
import com.xiaoyu.vo.common.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    private final LikeServiceImpl likeService;
    private final CommentServiceImpl commentService;
    private final ShareServiceImpl shareService;
    private final FavServiceImpl favService;

    @Override
    public PageResult<PostVO> getPosts(PostQueryDTO postQueryDTO) {
        // 构建查询条件
        QueryWrapper<PostPO> queryWrapper = buildQueryWrapper(postQueryDTO);
        // 创建分页对象
        Page<PostPO> page = new Page<>(postQueryDTO.getPage(), postQueryDTO.getSize());
        
        // 执行分页查询
        IPage<PostPO> postPage = postMapper.selectPage(page, queryWrapper);
        
        // 转换为VO并填充用户操作状态
        List<PostVO> postVOList = postPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        // 返回分页结果
        return PageResult.of(postVOList, postQueryDTO.getPage(), postQueryDTO.getSize(), postPage.getTotal());
    }
    
    @Override
    public PostVO getPostDetail(Long postId) {
        // 根据ID查询动态
        PostPO postPO = postMapper.selectById(postId);
        
        // 检查动态是否存在
        if (postPO == null) {
            throw new RuntimeException("动态不存在");
        }
        
        // 检查动态状态
        if (!"PUBLISHED".equals(postPO.getStatus().name())) {
            throw new RuntimeException("动态不可访问");
        }
        
        // 权限检查
        Long currentUserId = BaseContext.getCurrentId();
        if (!hasViewPermission(postPO, currentUserId)) {
            throw new RuntimeException("无权限查看该动态");
        }
        
        // 转换为VO并返回
        return convertToVO(postPO);
    }
    
    /**
     * 构建查询条件
     */
    private QueryWrapper<PostPO> buildQueryWrapper(PostQueryDTO postQueryDTO) {
        QueryWrapper<PostPO> queryWrapper = new QueryWrapper<>();
        
        // 只查询已发布的动态
        queryWrapper.eq("status", "PUBLISHED");
        
        // 根据查询类型添加条件
        String type = postQueryDTO.getType();
        if ("user".equals(type) && postQueryDTO.getUserId() != null) {
            // 查询指定用户的动态
            queryWrapper.eq("user_id", postQueryDTO.getUserId());
        } else if ("campus".equals(type) && postQueryDTO.getCampusId() != null) {
            // 查询指定校园的动态
            queryWrapper.eq("campus_id", postQueryDTO.getCampusId());
        } else if ("timeline".equals(type)) {
            // 时间线：查询当前用户可见的动态（公开 + 好友 + 校园）
            Long currentUserId = BaseContext.getCurrentId();
            if (currentUserId != null) {
                queryWrapper.and(wrapper -> wrapper
                    .eq("visibility", "PUBLIC")
                    .or().eq("visibility", "FRIEND") // 这里应该结合好友关系表查询
                    .or().eq("visibility", "CAMPUS") // 这里应该结合校园关系查询
                );
            } else {
                // 未登录用户
                queryWrapper.eq("visibility", "PUBLIC");
            }
        } else {
            queryWrapper.eq("visibility", "PUBLIC");
        }
        
        // 排序
        if ("hot".equals(postQueryDTO.getSort())) {
            // 热门排序：按点赞数、评论数、创建时间综合排序
            queryWrapper.orderByDesc("like_cnt", "comment_cnt", "created_at");
        } else {
            // 最新排序：按创建时间倒序，置顶优先
            queryWrapper.orderByDesc("is_top", "created_at");
        }
        
        return queryWrapper;
    }
    
    /**
     * 检查用户是否有权限查看动态
     */
    private boolean hasViewPermission(PostPO postPO, Long currentUserId) {
        String visibility = postPO.getVisibility().name();
        
        // 公开动态，所有人可见
        if ("PUBLIC".equals(visibility)) {
            return true;
        }
        
        // 未登录用户只能看公开动态
        if (currentUserId == null) {
            return false;
        }
        
        // 自己的动态总是可见
        if (postPO.getUserId().equals(currentUserId)) {
            return true;
        }
        
        // 好友可见：需要检查好友关系
        if ("FRIEND".equals(visibility)) {
            // TODO: 实现好友关系检查
            // return friendService.isFriend(postPO.getUserId(), currentUserId);
            return true; // 暂时返回true，等待好友服务实现
        }
        
        // 校园可见：需要检查是否同校园
        if ("CAMPUS".equals(visibility)) {
            // TODO: 实现校园关系检查
            // return userService.isSameCampus(postPO.getUserId(), currentUserId);
            return true;
        }
        
        return false;
    }
    
    /**
     * 转换为VO对象
     */
    private PostVO convertToVO(PostPO postPO) {
        PostVO postVO = new PostVO();
        postVO.setId(postPO.getId());
        postVO.setTitle(postPO.getTitle());
        postVO.setContent(postPO.getContent());
        postVO.setCampusId(postPO.getCampusId());
        postVO.setVisibility(postPO.getVisibility().name());
        postVO.setPoiName(postPO.getPoiName());
        postVO.setIsTop(postPO.getIsTop());
        postVO.setStatus(postPO.getStatus().name());
        postVO.setCreatedAt(postPO.getCreatedAt());
        postVO.setUpdatedAt(postPO.getUpdatedAt());
        
        // 填充用户信息这里需要根据userId查询用户信息
        // TODO: 实现用户信息查询和转换
        
        // 填充统计信息
        PostStatsVO stats = new PostStatsVO();
        // TODO: 实现统计数据查询
        stats.setShareCnt((int)shareService.getShareCount(postPO.getId(),"POST"));
        stats.setLikeCnt((int)likeService.getLikeCount(postPO.getId(), "POST"));
         stats.setCommentCnt((int)commentService.getCommentCount(postPO.getId(),"POST"));
        postVO.setStats(stats);
        
        // 填充当前用户的操作状态
        Long currentUserId = BaseContext.getCurrentId();
        if (currentUserId != null) {
            PostUserActionsVO userActions = new PostUserActionsVO();
//            // 暂时设置默认值，等待服务接口方法确认
//            userActions.setIsLiked(false);
//            userActions.setIsFavorited(false);
            // TODO: 实现用户操作状态查询
             userActions.setIsLiked(likeService.isLiked(postPO.getId(), currentUserId, "POST"));
             userActions.setIsFavorited(favService.isFavorited(postPO.getId(), currentUserId, "POST"));
            postVO.setUserActions(userActions);
        }
        
        // TODO: 填充文件列表、话题等关联数据
        
        return postVO;
    }

    /**
     * 更新动态
     * @param postUpdateDTO
     * @param postId
     */
    public void updatePost(PostUpdateDTO postUpdateDTO, Long postId){
        PostPO postPO = postMapper.selectById(postId);
        if (postPO == null) {
            return ;
        }
        if (postUpdateDTO.getTitle() != null) {
            postPO.setTitle(postUpdateDTO.getTitle());
        }
        if (postUpdateDTO.getContent() != null) {
            postPO.setContent(postUpdateDTO.getContent());
        }
        if (postUpdateDTO.getCampusId() != null) {
            postPO.setCampusId(postUpdateDTO.getCampusId());
        }
        if (postUpdateDTO.getVisibility() != null) {
            postPO.setVisibility(PostPO.Visibility.valueOf(postUpdateDTO.getVisibility()));
        }
        if (postUpdateDTO.getIsTop() != null) {
            postPO.setIsTop(postUpdateDTO.getIsTop());
        }
        postMapper.updateById(postPO);
    }
}
