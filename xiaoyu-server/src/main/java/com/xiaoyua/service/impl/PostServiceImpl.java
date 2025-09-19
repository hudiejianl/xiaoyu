package com.xiaoyua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoyua.context.BaseContext;
import com.xiaoyua.dto.post.PostCreateDTO;
import com.xiaoyua.dto.post.PostQueryDTO;
import com.xiaoyua.dto.post.PostUpdateDTO;
import com.xiaoyua.entity.PostPO;
import com.xiaoyua.entity.PostFilePO;
import com.xiaoyua.entity.PostStatPO;
import com.xiaoyua.mapper.PostMapper;
import com.xiaoyua.mapper.PostFileMapper;
import com.xiaoyua.mapper.PostStatMapper;
import com.xiaoyua.service.LikeService;
import com.xiaoyua.service.PostService;
import com.xiaoyua.service.ShareService;
import com.xiaoyua.service.FavService;
import com.xiaoyua.service.CommentService;
import com.xiaoyua.service.MessageService;
import com.xiaoyua.mapper.UserMapper;
import com.xiaoyua.entity.UserPO;
import com.xiaoyua.vo.post.PostVO;
import com.xiaoyua.vo.post.PostStatsVO;
import com.xiaoyua.vo.post.PostUserActionsVO;
import com.xiaoyua.vo.common.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostFileMapper postFileMapper;

    // 依赖接口而非实现，降低耦合
    private final LikeService likeService;
    private final CommentService commentService;
    private final ShareService shareService;
    private final FavService favService;
    private final MessageService messageService;
    private final UserMapper userMapper;
    private final PostStatMapper postStatMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostVO createPost(PostCreateDTO postCreateDTO, Long userId) {
        // 基础校验
        if (postCreateDTO == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (postCreateDTO.getContent() == null || postCreateDTO.getContent().isBlank()) {
            throw new IllegalArgumentException("动态内容不能为空");
        }

        // 构建并保存 PostPO
        PostPO post = new PostPO();
        post.setUserId(userId);
        post.setTitle(postCreateDTO.getTitle());
        post.setContent(postCreateDTO.getContent());
        post.setCampusId(postCreateDTO.getCampusId());
        if (postCreateDTO.getVisibility() != null) {
            post.setVisibility(PostPO.Visibility.valueOf(postCreateDTO.getVisibility()));
        } else {
            post.setVisibility(PostPO.Visibility.PUBLIC);
        }
        post.setPoiLat(postCreateDTO.getPoiLat());
        post.setPoiLng(postCreateDTO.getPoiLng());
        post.setPoiName(postCreateDTO.getPoiName());
        post.setIsTop(0);
        post.setStatus(PostPO.Status.PUBLISHED);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        postMapper.insert(post);
        // 初始化统计记录
        postStatMapper.initIfAbsent(post.getId());

        // 关联文件（如果有）
        if (postCreateDTO.getFileIds() != null && !postCreateDTO.getFileIds().isEmpty()) {
            int sort = 0;
            for (Long fileId : postCreateDTO.getFileIds()) {
                PostFilePO rel = new PostFilePO();
                rel.setPostId(post.getId());
                rel.setFileId(fileId);
                rel.setSort(sort++);
                postFileMapper.insert(rel);
            }
        }

        // 返回详情或概要，这里复用现有转换
        return getPostDetail(post.getId());
    }

    @Override
    public PageResult<PostVO> listAll(Integer pageNum, Integer pageSize, String sort) {
        if (pageNum == null || pageNum < 1)
            pageNum = 1;
        if (pageSize == null || pageSize < 1)
            pageSize = 20;

        QueryWrapper<PostPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "PUBLISHED");

        Long currentUserId = BaseContext.getCurrentId();
        if (currentUserId == null) {
            // 未登录仅公开
            queryWrapper.eq("visibility", "PUBLIC");
        } else {
            // 登录：公开/好友/校园（简化：用 OR 组合；严格权限在详情或后续增强）
            queryWrapper.and(w -> w.eq("visibility", "PUBLIC")
                    .or().eq("visibility", "FRIEND")
                    .or().eq("visibility", "CAMPUS"));
        }

        if ("hot".equalsIgnoreCase(sort)) {
            queryWrapper.orderByDesc("like_cnt", "comment_cnt", "created_at");
        } else {
            queryWrapper.orderByDesc("is_top", "created_at");
        }

        Page<PostPO> page = new Page<>(pageNum, pageSize);
        IPage<PostPO> postPage = postMapper.selectPage(page, queryWrapper);
        List<PostVO> vos = postPage.getRecords().stream().map(this::convertToVO)
                .collect(java.util.stream.Collectors.toList());
        return PageResult.of(vos, pageNum, pageSize, postPage.getTotal());
    }

    @Override
    public PageResult<PostVO> listByUser(Long userId, Integer pageNum, Integer pageSize, String sort) {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("userId 无效");
        if (pageNum == null || pageNum < 1)
            pageNum = 1;
        if (pageSize == null || pageSize < 1)
            pageSize = 20;

        QueryWrapper<PostPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "PUBLISHED");
        queryWrapper.eq("user_id", userId);

        Long viewerId = BaseContext.getCurrentId();
        if (viewerId == null) {
            queryWrapper.eq("visibility", "PUBLIC");
        } else if (!userId.equals(viewerId)) {
            boolean isFriend = false;
            boolean sameCampus = false;
            try {
                isFriend = messageService.isFriend(userId, viewerId);
            } catch (Exception ignored) {
            }
            try {
                var owner = userMapper.selectById(userId);
                var viewer = userMapper.selectById(viewerId);
                sameCampus = owner != null && viewer != null && owner.getCampusId() != null
                        && owner.getCampusId().equals(viewer.getCampusId());
            } catch (Exception ignored) {
            }

            // 仅允许可见范围
            if (isFriend && sameCampus) {
                queryWrapper.and(w -> w.eq("visibility", "PUBLIC").or().eq("visibility", "FRIEND").or().eq("visibility",
                        "CAMPUS"));
            } else if (isFriend) {
                queryWrapper.and(w -> w.eq("visibility", "PUBLIC").or().eq("visibility", "FRIEND"));
            } else if (sameCampus) {
                queryWrapper.and(w -> w.eq("visibility", "PUBLIC").or().eq("visibility", "CAMPUS"));
            } else {
                queryWrapper.eq("visibility", "PUBLIC");
            }
        }

        if ("hot".equalsIgnoreCase(sort)) {
            queryWrapper.orderByDesc("like_cnt", "comment_cnt", "created_at");
        } else {
            queryWrapper.orderByDesc("is_top", "created_at");
        }

        Page<PostPO> page = new Page<>(pageNum, pageSize);
        IPage<PostPO> postPage = postMapper.selectPage(page, queryWrapper);
        List<PostVO> vos = postPage.getRecords().stream().map(this::convertToVO)
                .collect(java.util.stream.Collectors.toList());
        return PageResult.of(vos, pageNum, pageSize, postPage.getTotal());
    }

    @Override
    public java.util.List<PostVO> listHot(Integer limit) {
        int topN = (limit == null || limit <= 0) ? 10 : limit;
        java.util.List<PostPO> list = postMapper.selectHotPosts(topN);
        return list.stream().map(this::convertToVO).collect(java.util.stream.Collectors.toList());
    }

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

        // 增加浏览量
        try { postStatMapper.incView(postId); } catch (Exception ignored) {}

        // 转换为VO并返回（包含最新统计）
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
            try {
                return messageService.isFriend(postPO.getUserId(), currentUserId);
            } catch (Exception e) {
                return false;
            }
        }
        // 校园可见：需要检查是否同校园
        if ("CAMPUS".equals(visibility)) {
            try {
                UserPO owner = userMapper.selectById(postPO.getUserId());
                UserPO viewer = userMapper.selectById(currentUserId);
                if (owner == null || viewer == null)
                    return false;
                return owner.getCampusId() != null && owner.getCampusId().equals(viewer.getCampusId());
            } catch (Exception e) {
                return false;
            }
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

        // 填充统计信息（来自 post_stats）
        PostStatsVO stats = new PostStatsVO();
        try {
            PostStatPO statPO = postStatMapper.selectById(postPO.getId());
            if (statPO != null) {
                stats.setViewCnt(statPO.getViewCnt());
                stats.setLikeCnt(statPO.getLikeCnt());
                stats.setFavCnt(statPO.getFavCnt());
                stats.setCommentCnt(statPO.getCommentCnt());
                stats.setShareCnt(statPO.getShareCnt());
            } else {
                stats.setViewCnt(0);
                stats.setLikeCnt(0);
                stats.setFavCnt(0);
                stats.setCommentCnt(0);
                stats.setShareCnt(0);
            }
        } catch (Exception e) {
            stats.setViewCnt(0);
            stats.setLikeCnt(0);
            stats.setFavCnt(0);
            stats.setCommentCnt(0);
            stats.setShareCnt(0);
        }
        postVO.setStats(stats);

        // 填充当前用户的操作状态
        Long currentUserId = BaseContext.getCurrentId();
        if (currentUserId != null) {
            PostUserActionsVO userActions = new PostUserActionsVO();
            // // 暂时设置默认值，等待服务接口方法确认
            // userActions.setIsLiked(false);
            // userActions.setIsFavorited(false);
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
     * 
     * @param postUpdateDTO
     * @param postId
     */
    public void updatePost(PostUpdateDTO postUpdateDTO, Long postId) {
        PostPO postPO = postMapper.selectById(postId);
        if (postPO == null) {
            return;
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
