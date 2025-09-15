package com.xiaoyu.controller.dynamic;

import com.xiaoyu.context.BaseContext;
import com.xiaoyu.dto.post.PostQueryDTO;
import com.xiaoyu.dto.post.PostUpdateDTO;
import com.xiaoyu.service.PostService;
import com.xiaoyu.service.CommentService;
import com.xiaoyu.service.LikeService;
import com.xiaoyu.service.FavService;
import com.xiaoyu.service.ShareService;
import com.xiaoyu.vo.post.PostVO;
import com.xiaoyu.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@Slf4j
public class PostController {
    @Autowired
    private PostService postService;
    
    @Autowired
    private LikeService likeService;
    
    @Autowired
    private FavService favService;
    
    @Autowired
    private ShareService shareService;
    
    @Autowired
    private CommentService commentService;

    @PostMapping("/{post_id}/like")
    public Result addLike(@PathVariable("post_id") Long postId) {
        log.info("addLike postId={}", postId);
        Long userId = BaseContext.getCurrentId();
        likeService.addLike(postId, userId, "POST");
        
        // 返回最新的点赞数量
        long likeCount = likeService.getLikeCount(postId, "POST");
        return Result.success("点赞成功", Map.of("like_cnt", likeCount));
    }

    @DeleteMapping("/{post_id}/like")
    public Result deleteLike(@PathVariable("post_id") Long postId) {
        log.info("deleteLike postId={}", postId);
        Long userId = BaseContext.getCurrentId();
        likeService.deleteLike(postId, userId, "POST");
        
        // 返回最新的点赞数量
        long likeCount = likeService.getLikeCount(postId, "POST");
        return Result.success("取消点赞成功", Map.of("like_cnt", likeCount));
    }

    @PostMapping("/{post_id}/favorite")
    public Result addFav(@PathVariable("post_id") Long postId) {
        log.info("addFav postId={}", postId);
        Long userId = BaseContext.getCurrentId();
        favService.addFavorite(postId, userId, "POST");
        long favCount = favService.getFavoriteCount(postId, "POST");
        return Result.success("收藏成功", Map.of("fav_cnt", favCount));
    }
    
    @DeleteMapping("/{post_id}/favorite")
    public Result deleteFav(@PathVariable("post_id") Long postId) {
        log.info("deleteFav postId={}", postId);
        Long userId = BaseContext.getCurrentId();
        favService.deleteFavorite(postId, userId, "POST");
        long favCount = favService.getFavoriteCount(postId, "POST");
        return Result.success("取消收藏成功",favCount);
    }

    @PostMapping("/{post_id}/share")
    public Result addShare(@PathVariable("post_id") Long postId){
        log.info("addShare postId={}", postId);
        shareService.addShare(postId, BaseContext.getCurrentId());
        return Result.success("转发成功");
    }

    @GetMapping
    public Result getPosts(PostQueryDTO postQueryDTO) {
        log.info("getPosts postQueryDTO={}", postQueryDTO);
        
        // 参数验证
        if (postQueryDTO == null) {
            postQueryDTO = new PostQueryDTO();
        }
        postQueryDTO.validate();
        
        // 调用服务层获取动态列表
        var pageResult = postService.getPosts(postQueryDTO);
        
        return Result.success("success", pageResult);
    }

    @GetMapping("/{post_id}")
    public Result getPostDetail(@PathVariable("post_id") Long postId) {
        log.info("getPostDetail postId={}", postId);
        
        // 参数验证
        if (postId == null || postId <= 0) {
            return Result.error("动态ID不能为空");
        }
        
        try {
            // 调用服务层获取动态详情
            PostVO postVO = postService.getPostDetail(postId);
            return Result.success("获取成功", postVO);
        } catch (RuntimeException e) {
            log.warn("获取动态详情失败: postId={}, error={}", postId, e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{post_id}")
    public Result updatePost(@RequestBody PostUpdateDTO postUpdateDTO, @PathVariable("post_id") Long postId) {
        log.info("更新动态", postUpdateDTO);
        postService.updatePost(postUpdateDTO,postId);
        return Result.success("更新成功");
    }


}
