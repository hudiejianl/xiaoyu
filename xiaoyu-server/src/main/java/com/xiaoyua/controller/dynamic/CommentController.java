package com.xiaoyua.controller.dynamic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyua.context.BaseContext;
import com.xiaoyua.dto.comment.CommentCreateDTO;
import com.xiaoyua.result.Result;
import com.xiaoyua.service.CommentService;
import com.xiaoyua.service.LikeService;
import com.xiaoyua.vo.comment.CommentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Min;
import com.xiaoyua.common.enums.TargetType;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/comments")
@Slf4j
@Tag(name = "评论管理", description = "评论相关接口")
@Validated
public class CommentController {
    @Autowired
    LikeService likeService;

    @Autowired
    private CommentService commentService;
    @PostMapping("/{post_id}")
    @Operation(summary = "发表评论", description = "对动态发表评论")
    public Result addComment(
            @Parameter(description = "动态ID") @PathVariable("post_id") Long postId,
            @RequestBody @Valid CommentCreateDTO comment){
        log.info("addComment postId={}, comment={}", postId, comment);
        comment.setPostId(postId); // 确保设置正确的postId
        commentService.addComment(comment);
        return Result.success("评论成功");
    }

    @DeleteMapping("/{comment_id}")
    @Operation(summary = "删除评论", description = "删除自己的评论")
    public Result deleteComment(
            @Parameter(description = "评论ID") @PathVariable("comment_id") Long commentId){
        log.info("deleteComment commentId={}", commentId);
        commentService.deleteComment(commentId);
        return Result.success("删除评论成功");
    }

    @GetMapping
    @Operation(summary = "获取动态评论列表", description = "获取指定动态的评论列表")
    public Result<IPage<CommentVO>> getCommentsByPostId(
            @Parameter(description = "动态ID") @RequestParam("post_id") Long postId,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "20") @Min(1) Integer size,
            @Parameter(description = "排序方式") @RequestParam(defaultValue = "latest") String sort) {
        log.info("getCommentsByPostId postId={}, page={}, size={}, sort={}", postId, page, size, sort);
        IPage<CommentVO> comments = commentService.getComments(postId, page, size, sort);
        return Result.success(comments);
    }

    @PostMapping("/{comment_id}/like")
    @Operation(summary = "点赞评论", description = "对评论进行点赞")
    public Result likeComment(
            @Parameter(description = "评论ID") @PathVariable("comment_id") Long commentId) {
        Long userId = BaseContext.getCurrentId();
        log.info("likeComment commentId={}, userId={}", commentId, userId);
        likeService.addLike(commentId, userId, TargetType.COMMENT);
        return Result.success("点赞成功");
    }

    @DeleteMapping("/{comment_id}/like")
    @Operation(summary = "取消点赞评论", description = "取消对评论的点赞")
    public Result unlikeComment(
            @Parameter(description = "评论ID") @PathVariable("comment_id") Long commentId) {
        Long userId = BaseContext.getCurrentId();
        log.info("unlikeComment commentId={}, userId={}", commentId, userId);
        likeService.deleteLike(commentId, userId, TargetType.COMMENT);
        return Result.success("取消点赞成功");
    }
}
