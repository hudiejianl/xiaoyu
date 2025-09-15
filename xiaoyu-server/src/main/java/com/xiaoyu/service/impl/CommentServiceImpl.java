package com.xiaoyu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaoyu.context.BaseContext;
import com.xiaoyu.dto.comment.CommentCreateDTO;
import com.xiaoyu.entity.CommentPO;
import com.xiaoyu.entity.LikePO;
import com.xiaoyu.entity.NotificationPO;
import com.xiaoyu.entity.PostPO;
import com.xiaoyu.entity.UserPO;
import com.xiaoyu.mapper.CommentMapper;
import com.xiaoyu.mapper.LikeMapper;
import com.xiaoyu.mapper.PostMapper;
import com.xiaoyu.mapper.UserMapper;
import com.xiaoyu.service.CommentService;
import com.xiaoyu.service.PushService;
import com.xiaoyu.vo.comment.CommentVO;
import com.xiaoyu.vo.user.UserSimpleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    LikeMapper likeMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PushService pushService;

    @Override
    public void addComment(CommentCreateDTO comment) {
        CommentPO commentPO=new CommentPO();
        commentPO.setUserId(BaseContext.getCurrentId());
        commentPO.setContent(comment.getContent());
        commentPO.setStatus(CommentPO.Status.VISIBLE);
        commentPO.setItemType(CommentPO.ItemType.POST);
        commentPO.setAtUsers(
                comment.getAtUsers() == null ? null :
                        comment.getAtUsers().stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(","))
        );
        commentPO.setItemId(comment.getPostId());
        commentPO.setParentId(comment.getParentId());
        commentPO.setCreatedAt(LocalDateTime.now());
        commentMapper.insert(commentPO);
        
        // 创建评论通知
        createCommentNotification(commentPO);
    }

    public void deleteComment(Long commentId) {
        commentMapper.deleteById(commentId);

    }

    /**
     * 查询一篇文章的全部评论（含二级回复）
     */
    @Override
    public IPage<CommentVO> getComments(Long postId, int page, int size, String sort) {
        //先查一级评论parent_id = 0
        IPage<CommentPO> poPage = commentMapper.selectPage(
                new Page<>(page, size),
                new QueryWrapper<CommentPO>()
                        .eq("item_id", postId)
                        .eq("item_type", "POST")
                        .eq("parent_id", 0)          // 只查根评论
                        .orderBy(sort == null || sort.equals("latest"), false, "created_at")
                        .orderBy("hot".equals(sort), false, "like_cnt", "created_at")
        );

        //取出本页所有一级评论 id
        List<Long> rootIds = poPage.getRecords()
                .stream()
                .map(CommentPO::getId)
                .collect(Collectors.toList());

        //一次性查二级评论
        List<CommentPO> subList = CollUtil.isEmpty(rootIds) ? Collections.emptyList()
                : commentMapper.selectList(
                new QueryWrapper<CommentPO>()
                        .in("parent_id", rootIds)
                        .orderByAsc("created_at"));

        //把二级按 parent_id 分组
        Map<Long, List<CommentPO>> subMap = subList.stream()
                .collect(Collectors.groupingBy(CommentPO::getParentId));

        /*拼Vo（一级 二级 user @用户 点赞信息*/
        List<CommentVO> voList = poPage.getRecords().stream().map(root -> {
            CommentVO vo = new CommentVO();
            BeanUtil.copyProperties(root, vo);          // 同名字段快速拷贝（ Hutool 工具，Spring 的 BeanUtils 也行）

            // 发评论的用户信息
            vo.setUser(buildUserVo(root.getUserId()));

            /*@用户 JSON 数组  -> List<UserVo>*/
            vo.setAtUsers(parseAtUsers(root.getAtUsers()));

            // 5.3 当前用户是否点赞
            Long currUserId = root.getUserId();
            boolean isLiked = likeMapper.selectCount(
                    new LambdaQueryWrapper<LikePO>()
                            .eq(LikePO::getUserId, currUserId)
                            .eq(LikePO::getItemId, root.getItemId())
                            .eq(LikePO::getItemType, "COMMENT")
            ) > 0;

            vo.setIsLiked(isLiked);

            // 5.4 二级回复
            List<CommentVO> replies = Optional.ofNullable(subMap.get(root.getId()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(sub -> buildSubCommentVo(sub))
                    .collect(Collectors.toList());
            vo.setReplies(replies);
            vo.setReplyCount(subMap.getOrDefault(root.getId(), Collections.emptyList()).size());

            return vo;
        }).collect(Collectors.toList());

        //6. 把 List 重新包成 IPage 返
        IPage<CommentVO> voPage = new Page<>(page, size);
        voPage.setRecords(voList);
        voPage.setTotal(poPage.getTotal());
        return voPage;
    }

    /*工具方*/

    private UserSimpleVO buildUserVo(Long userId) {
        UserPO user = userMapper.selectById(userId);
        return user == null ? null : new UserSimpleVO(
                user.getId(),user.getNickname(),user.getAvatarUrl(),user.getGender(),user.getCampusId(),
                user.getIsRealName(),user.getCreatedAt()
        );
    }

    private List<UserSimpleVO> parseAtUsers(String json) {
        if (StrUtil.isBlank(json)) return Collections.emptyList();
        try {
            return JSONUtil.toList(JSONUtil.parseArray(json), UserSimpleVO.class);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private CommentVO buildSubCommentVo(CommentPO sub) {
        CommentVO vo = new CommentVO();
        BeanUtil.copyProperties(sub, vo);
        vo.setUser(buildUserVo(sub.getUserId()));
        vo.setAtUsers(parseAtUsers(sub.getAtUsers()));


        boolean isLiked = likeMapper.selectCount(
                new LambdaQueryWrapper<LikePO>()
                        .eq(LikePO::getUserId, sub.getUserId())
                        .eq(LikePO::getItemId, sub.getItemId())
                        .eq(LikePO::getItemType, "COMMENT")
        ) > 0;
        vo.setIsLiked(isLiked);

        return vo;
    }

    /**
     * 获取一个post的评论的数量
     * @param postId
     * @param type
     */
    @Override
    public long getCommentCount(Long postId,String type) {
        return commentMapper.selectCount(
                new QueryWrapper<CommentPO>()
                        .eq("item_id", postId)
                        .eq("item_type", type.toUpperCase())
        );
    }
    
    /**
     * 创建评论通知
     */
    private void createCommentNotification(CommentPO commentPO) {
        try {
            Long fromUserId = commentPO.getUserId();
            Long postId = commentPO.getItemId();
            
            // 获取动态作者ID
            PostPO post = postMapper.selectById(postId);
            if (post == null) {
                return;
            }
            
            Long toUserId = post.getUserId();
            
            // 不给自己发通知
            if (toUserId.equals(fromUserId)) {
                return;
            }
            
            // 获取评论用户信息
            UserPO fromUser = userMapper.selectById(fromUserId);
            if (fromUser == null) {
                return;
            }
            
            // 构建通知内容
            String title = "收到新的评论";
            String content = String.format("%s 评论了你的动态", fromUser.getNickname());
            
            // 如果是回复评论，需要特殊处理
            if (commentPO.getParentId() != null && commentPO.getParentId() > 0) {
                // 获取被回复的评论
                CommentPO parentComment = commentMapper.selectById(commentPO.getParentId());
                if (parentComment != null) {
                    Long parentUserId = parentComment.getUserId();
                    // 如果回复的不是动态作者，则通知被回复的用户
                    if (!parentUserId.equals(toUserId) && !parentUserId.equals(fromUserId)) {
                        createReplyNotification(commentPO, parentComment, fromUser);
                    }
                }
                content = String.format("%s 回复了你的动态", fromUser.getNickname());
            }
            
            // 使用PushService发送通知
            pushService.pushNotification(
                toUserId,
                NotificationPO.Type.COMMENT.name(),
                title,
                content,
                postId,
                NotificationPO.RefType.POST.name(),
                fromUserId
            );
            
        } catch (Exception e) {
            log.error("创建评论通知失败: commentId={}, error={}", 
                commentPO.getId(), e.getMessage(), e);
        }
    }
    
    /**
     * 创建回复通知
     */
    private void createReplyNotification(CommentPO replyComment, CommentPO parentComment, UserPO fromUser) {
        try {
            Long toUserId = parentComment.getUserId();
            
            // 构建通知内容
            String title = "收到新的回复";
            String content = String.format("%s 回复了你的评论", fromUser.getNickname());
            
            // 使用PushService发送通知
            pushService.pushNotification(
                toUserId,
                NotificationPO.Type.COMMENT.name(),
                title,
                content,
                replyComment.getItemId(), // 关联到动态ID
                NotificationPO.RefType.POST.name(),
                fromUser.getId()
            );
            
        } catch (Exception e) {
            log.error("创建回复通知失败: replyCommentId={}, parentCommentId={}, error={}", 
                replyComment.getId(), parentComment.getId(), e.getMessage(), e);
        }
    }

}
