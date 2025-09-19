package com.xiaoyua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoyua.context.BaseContext;
import com.xiaoyua.entity.*;
import com.xiaoyua.mapper.*;
import com.xiaoyua.service.FavService;
import com.xiaoyua.service.LikeService;
import com.xiaoyua.service.TopicService;
import com.xiaoyua.vo.file.FileSimpleVO;
import com.xiaoyua.vo.post.PostStatsVO;
import com.xiaoyua.vo.post.PostUserActionsVO;
import com.xiaoyua.vo.post.PostVO;
import com.xiaoyua.vo.topic.TopicSimpleVO;
import com.xiaoyua.vo.user.UserSimpleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 话题服务实现类
 */
@Service
@Slf4j
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private TopicPostMapper topicPostMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FavService favService;
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private ShareMapper shareMapper;
    
    @Autowired
    private FileMapper fileMapper;

    @Override
    public List<TopicSimpleVO> getHotTopics(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        log.info("获取热门话题列表，limit={}", limit);

        QueryWrapper<TopicPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("post_cnt", 0)
                .orderByDesc("post_cnt")
                .last("LIMIT " + limit);

        List<TopicPO> topicPOs = topicMapper.selectList(queryWrapper);
        return topicPOs.stream().map(this::convertToSimpleVO).collect(Collectors.toList());
    }

    @Override
    public List<TopicSimpleVO> getAllTopics() {
        log.info("获取所有话题列表");
        QueryWrapper<TopicPO> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("post_cnt").orderByAsc("name");
        List<TopicPO> topics = topicMapper.selectList(wrapper);
        return topics.stream().map(this::convertToSimpleVO).collect(Collectors.toList());
    }

    @Override
    public TopicSimpleVO getTopicById(Long topicId) {
        log.info("获取话题详情，topicId={}", topicId);

        TopicPO topicPO = topicMapper.selectById(topicId);
        if (topicPO == null) {
            return null;
        }

        return convertToSimpleVO(topicPO);
    }

    @Override
    public IPage<PostVO> getPostsByTopicId(Long topicId, Integer page, Integer size, String sort) {
        if (page == null || page <= 0) {
            page = 1;
        }
        if (size == null || size <= 0) {
            size = 20;
        }
        if (sort == null) {
            sort = "latest";
        }

        log.info("获取话题下的动态列表，topicId={}, page={}, size={}, sort={}", topicId, page, size, sort);

        // 获取该话题下的所有动态ID
        QueryWrapper<TopicPostPO> topicPostWrapper = new QueryWrapper<>();
        topicPostWrapper.eq("topic_id", topicId);
        List<TopicPostPO> topicPosts = topicPostMapper.selectList(topicPostWrapper);

        if (topicPosts.isEmpty()) {
            // 如果没有关联的动态，返回空
            return new Page<>(page, size);
        }

        // 提取动态ID列表
        List<Long> postIds = topicPosts.stream()
                .map(TopicPostPO::getPostId)
                .collect(Collectors.toList());

        // 分页查询动态
        Page<PostPO> pageObj = new Page<>(page, size);
        QueryWrapper<PostPO> postWrapper = new QueryWrapper<>();
        postWrapper.in("id", postIds)
                .eq("status", "PUBLISHED"); // 只查询已发布的动态

        // 根据排序方式设置排序规则
        if ("hot".equals(sort)) {
            // 简但为按创建时间倒序
            postWrapper.orderByDesc("created_at");
        } else {
            // 最新排序
            postWrapper.orderByDesc("created_at");
        }

        IPage<PostPO> postPage = postMapper.selectPage(pageObj, postWrapper);

        // 转换为PostVO（这里简化处理，实际项目中需要关联查询用户信息、文件信息等）
        IPage<PostVO> result = new Page<>(page, size);
        result.setTotal(postPage.getTotal());
        result.setPages(postPage.getPages());

        List<PostVO> postVOs = postPage.getRecords().stream()
                .map(this::convertToPostVO)
                .collect(Collectors.toList());
        result.setRecords(postVOs);

        return result;
    }

    /**
     * 转换TopicPO为TopicSimpleVO
     */
    private TopicSimpleVO convertToSimpleVO(TopicPO topicPO) {
        TopicSimpleVO vo = new TopicSimpleVO();
        vo.setId(topicPO.getId());
        vo.setName(topicPO.getName());
        vo.setDescription(topicPO.getDescription());
        vo.setPostCount(topicPO.getPostCnt());
        return vo;
    }

    /**
     * 转换PostPO为PostVO
     */
    private PostVO convertToPostVO(PostPO postPO) {
        PostVO vo = new PostVO();

        // 基本信息
        vo.setId(postPO.getId());
        vo.setTitle(postPO.getTitle());
        vo.setContent(postPO.getContent());
        vo.setCampusId(postPO.getCampusId());
        vo.setVisibility(postPO.getVisibility() != null ? postPO.getVisibility().name() : null);
        vo.setPoiName(postPO.getPoiName());
        vo.setIsTop(postPO.getIsTop());
        vo.setStatus(postPO.getStatus() != null ? postPO.getStatus().name() : null);
        vo.setCreatedAt(postPO.getCreatedAt());
        vo.setUpdatedAt(postPO.getUpdatedAt());

        // 查询用户信息
        UserPO userPO = userMapper.selectById(postPO.getUserId());
        if (userPO != null) {
            UserSimpleVO userVO = new UserSimpleVO();
            userVO.setId(userPO.getId());
            userVO.setNickname(userPO.getNickname());
            userVO.setAvatarUrl(userPO.getAvatarUrl());
            userVO.setGender(userPO.getGender());
            userVO.setCampusId(userPO.getCampusId());
            userVO.setIsRealName(userPO.getIsRealName());
            userVO.setCreatedAt(userPO.getCreatedAt());
            vo.setUser(userVO);
        }

        // 统计信息
        PostStatsVO statsVO = new PostStatsVO();
        statsVO.setViewCnt(0); // 浏览数暂时设为0，实际项目中需要从统计表查询
        statsVO.setLikeCnt((int) likeService.getLikeCount(postPO.getId(), "POST"));
        statsVO.setCommentCnt(getCommentCount(postPO.getId()));
        statsVO.setShareCnt(getShareCount(postPO.getId()));
        vo.setStats(statsVO);

        // 用户操作状态
        PostUserActionsVO userActionsVO = new PostUserActionsVO();
        Long currentUserId = null;
        try {
            currentUserId = BaseContext.getCurrentId();
        } catch (Exception e) {
            // 用户未登录，设置默认值
        }

        if (currentUserId != null) {
            userActionsVO.setIsLiked(likeService.isLiked(postPO.getId(), currentUserId, "POST"));
            userActionsVO.setIsFavorited(favService.isFavorited(postPO.getId(), currentUserId, "POST"));
        } else {
            userActionsVO.setIsLiked(false);
            userActionsVO.setIsFavorited(false);
        }
        vo.setUserActions(userActionsVO);

        // 文件列表
        vo.setFiles(getPostFiles(postPO.getId()));
        
        // 话题列表
        vo.setTopics(getPostTopics(postPO.getId()));
        
        return vo;
    }
    
    /**
     * 获取动态的评论数量
     */
    private int getCommentCount(Long postId) {
        try {
            QueryWrapper<CommentPO> wrapper = new QueryWrapper<>();
            wrapper.eq("ref_id", postId)
                   .eq("ref_type", "POST")
                   .eq("status", "PUBLISHED");
            return Math.toIntExact(commentMapper.selectCount(wrapper));
        } catch (Exception e) {
            log.error("获取动态评论数量失败: postId={}, error={}", postId, e.getMessage());
            return 0;
        }
    }
    
    /**
     * 获取动态的分享数量
     */
    private int getShareCount(Long postId) {
        try {
            QueryWrapper<SharePO> wrapper = new QueryWrapper<>();
            wrapper.eq("ref_id", postId)
                   .eq("ref_type", "POST");
            return Math.toIntExact(shareMapper.selectCount(wrapper));
        } catch (Exception e) {
            log.error("获取动态分享数量失败: postId={}, error={}", postId, e.getMessage());
            return 0;
        }
    }
    
    /**
     * 获取动态关联的文件列表
     */
    private List<FileSimpleVO> getPostFiles(Long postId) {
        try {
            QueryWrapper<FilePO> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", postId) // 根据实际业务逻辑调整查询条件
                   .eq("biz_type", "POST");
            List<FilePO> files = fileMapper.selectList(wrapper);
            
            // 转换为FileSimpleVO
            return files.stream().map(file -> {
                FileSimpleVO fileVO = new FileSimpleVO();
                fileVO.setId(file.getId());
                fileVO.setFileUrl(file.getFileUrl());
                fileVO.setThumbnailUrl(file.getThumbUrl());
                fileVO.setFileSize(file.getSize() != null ? file.getSize().longValue() : 0L);
                // 根据bizType推断文件类型
                fileVO.setFileType(inferFileType(file.getBizType()));
                return fileVO;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取动态文件列表失败: postId={}, error={}", postId, e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 根据业务类型推断文件类型
     */
    private String inferFileType(FilePO.BizType bizType) {
        if (bizType == null) {
            return "UNKNOWN";
        }
        switch (bizType) {
            case AVATAR:
            case BG:
            case POST:
                return "IMAGE";
            default:
                return "DOCUMENT";
        }
    }
    
    /**
     * 获取动态关联的话题列表
     */
    private List<TopicSimpleVO> getPostTopics(Long postId) {
        try {
            // 查询动态关联的话题
            QueryWrapper<TopicPostPO> wrapper = new QueryWrapper<>();
            wrapper.eq("post_id", postId);
            List<TopicPostPO> topicPosts = topicPostMapper.selectList(wrapper);
            
            if (topicPosts.isEmpty()) {
                return new ArrayList<>();
            }
            
            // 获取话题ID列表
            List<Long> topicIds = topicPosts.stream()
                    .map(TopicPostPO::getTopicId)
                    .collect(Collectors.toList());
            
            // 查询话题详情
            List<TopicPO> topics = topicMapper.selectBatchIds(topicIds);
            
            // 转换为VO
            return topics.stream()
                    .map(this::convertToSimpleVO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取动态话题列表失败: postId={}, error={}", postId, e.getMessage());
            return new ArrayList<>();
        }
    }
}
