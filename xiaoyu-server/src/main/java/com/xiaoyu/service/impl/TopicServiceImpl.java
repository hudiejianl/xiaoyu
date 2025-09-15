package com.xiaoyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoyu.context.BaseContext;
import com.xiaoyu.entity.*;
import com.xiaoyu.mapper.*;
import com.xiaoyu.service.FavService;
import com.xiaoyu.service.LikeService;
import com.xiaoyu.service.TopicService;
import com.xiaoyu.vo.post.PostStatsVO;
import com.xiaoyu.vo.post.PostUserActionsVO;
import com.xiaoyu.vo.post.PostVO;
import com.xiaoyu.vo.topic.TopicSimpleVO;
import com.xiaoyu.vo.user.UserSimpleVO;
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

    @Override
    public List<TopicSimpleVO> getHotTopics(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        log.info("获取热门话题列表，limit={}", limit);
        
        // 使用MyBatis-Plus查询热门话题
        QueryWrapper<TopicPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("post_cnt", 0)  // 动态数量大于0
                   .orderByDesc("post_cnt")  // 按动态数量降序
                   .last("LIMIT " + limit);  // 限制数量
        
        List<TopicPO> topicPOs = topicMapper.selectList(queryWrapper);
        
        // 转换为VO
        return topicPOs.stream().map(this::convertToSimpleVO).collect(Collectors.toList());
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
        
        // 先查询话题-动态关联表，获取该话题下的所有动态ID
        QueryWrapper<TopicPostPO> topicPostWrapper = new QueryWrapper<>();
        topicPostWrapper.eq("topic_id", topicId);
        List<TopicPostPO> topicPosts = topicPostMapper.selectList(topicPostWrapper);
        
        if (topicPosts.isEmpty()) {
            // 如果没有关联的动态，返回空分页结果
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
                  .eq("status", "PUBLISHED");  // 只查询已发布的动态
        
        // 根据排序方式设置排序规则
        if ("hot".equals(sort)) {
            // 热门排序：可以根据点赞数、评论数等排序，这里简化为按创建时间倒序
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
     * 转换PostPO为PostVO（完整版本）
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
        statsVO.setCommentCnt(0); // 评论数暂时设为0，实际项目中需要从评论表查询
        statsVO.setShareCnt(0); // 分享数暂时设为0，实际项目中需要从分享表查询
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
        
        // 文件列表、话题列表、标签列表暂时设为空列表
        // 实际项目中需要关联查询相关表
        vo.setFiles(new ArrayList<>());
        vo.setTopics(new ArrayList<>());
        vo.setTags(new ArrayList<>());
        
        return vo;
    }
}
