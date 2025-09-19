package com.xiaoyua.controller.dynamic;

import com.xiaoyua.context.BaseContext;
import com.xiaoyua.dto.post.PostCreateDTO;
import com.xiaoyua.dto.post.PostForm;
import com.xiaoyua.dto.post.PostUpdateDTO;
import com.xiaoyua.service.PostService;
import com.xiaoyua.service.CommentService;
import com.xiaoyua.service.LikeService;
import com.xiaoyua.service.FavService;
import com.xiaoyua.service.ShareService;
import com.xiaoyua.service.FileService;
import com.xiaoyua.vo.post.PostVO;
import com.xiaoyua.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import com.xiaoyua.common.enums.TargetType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/posts")
@Slf4j
@Validated
@Tag(name = "动态管理", description = "动态相关接口")
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
    private FileService fileService;


    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "发布动态（@ModelAttribute 混合表单：表单字段 + files）")
    public Result<PostVO> createPost(@Valid @ModelAttribute PostForm form) {
        Long userId = BaseContext.getCurrentId();

        // 将表单映射到 DTO
        PostCreateDTO dto = new PostCreateDTO();
        dto.setTitle(form.getTitle());
        dto.setContent(form.getContent());
        dto.setCampusId(form.getCampusId());
        dto.setVisibility(form.getVisibility());
        dto.setPoiLat(form.getPoiLat());
        dto.setPoiLng(form.getPoiLng());
        dto.setPoiName(form.getPoiName());

        // 处理文件
        List<MultipartFile> files = form.getFiles();
        if (files != null && !files.isEmpty()) {
            List<Long> fileIds = new ArrayList<>();
            for (MultipartFile file : files) {
                var fileVO = fileService.uploadFile(file, "POST", userId);
                if (fileVO != null && fileVO.getId() != null) {
                    fileIds.add(fileVO.getId());
                }
            }
            dto.setFileIds(fileIds);
        }

        PostVO postVO = postService.createPost(dto, userId);
        return Result.success("发布成功", postVO);
    }

    @PostMapping("/{post_id}/like")
    @Operation(summary = "点赞动态")
    public Result addLike(@PathVariable("post_id") @Min(1) Long postId) {
        log.info("addLike postId={}", postId);
        Long userId = BaseContext.getCurrentId();
        likeService.addLike(postId, userId, TargetType.POST);
        // 返回最新的点赞数量
        long likeCount = likeService.getLikeCount(postId, TargetType.POST);
        return Result.success("点赞成功", Map.of("like_cnt", likeCount));
    }

    @DeleteMapping("/{post_id}/like")
    @Operation(summary = "取消点赞动态")
    public Result deleteLike(@PathVariable("post_id") @Min(1) Long postId) {
        log.info("deleteLike postId={}", postId);
        Long userId = BaseContext.getCurrentId();
        likeService.deleteLike(postId, userId, TargetType.POST);
        
        // 返回最新的点赞数量
        long likeCount = likeService.getLikeCount(postId, TargetType.POST);
        return Result.success("取消点赞成功", Map.of("like_cnt", likeCount));
    }

    @PostMapping("/{post_id}/favorite")
    @Operation(summary = "收藏动态")
    public Result addFav(@PathVariable("post_id") @Min(1) Long postId) {
        log.info("addFav postId={}", postId);
        Long userId = BaseContext.getCurrentId();
        favService.addFavorite(postId, userId, TargetType.POST);
        long favCount = favService.getFavoriteCount(postId, TargetType.POST);
        return Result.success("收藏成功", Map.of("fav_cnt", favCount));
    }
    
    @DeleteMapping("/{post_id}/favorite")
    @Operation(summary = "取消收藏动态")
    public Result deleteFav(@PathVariable("post_id") @Min(1) Long postId) {
        log.info("deleteFav postId={}", postId);
        Long userId = BaseContext.getCurrentId();
        favService.deleteFavorite(postId, userId, TargetType.POST);
        long favCount = favService.getFavoriteCount(postId, TargetType.POST);
        return Result.success("取消收藏成功", Map.of("fav_cnt", favCount));
    }

    @PostMapping("/{post_id}/share")
    @Operation(summary = "转发动态")
    public Result addShare(@PathVariable("post_id") @Min(1) Long postId){
        log.info("addShare postId={}", postId);
        shareService.addShare(postId, BaseContext.getCurrentId());
        return Result.success("转发成功");
    }

    @GetMapping("/hot")
    @Operation(summary = "获取热门动态（按浏览数倒序，默认前10条）")
    public Result<List<PostVO>> getHotPosts(@RequestParam(value = "limit", required = false) Integer limit) {
        log.info("listHot limit={}", limit);
        var list = postService.listHot(limit);
        return Result.success("success", list);
    }

    @GetMapping
    @Operation(summary = "获取全部动态列表")
    public Result getAllPosts(@RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "size", required = false) Integer size,
                              @RequestParam(value = "sort", required = false) String sort) {
        log.info("listAll page={}, size={}, sort={}", page, size, sort);
        var pageResult = postService.listAll(page, size, sort);
        return Result.success("success", pageResult);
    }

    @GetMapping("/{user_id}")
    @Operation(summary = "获取指定用户的动态列表")
    public Result getPostsByUser(@PathVariable("user_id") @Min(1) Long userId,
                                 @RequestParam(value = "page", required = false) Integer page,
                                 @RequestParam(value = "size", required = false) Integer size,
                                 @RequestParam(value = "sort", required = false) String sort) {
        log.info("listByUser userId={}, page={}, size={}, sort={}", userId, page, size, sort);
        var pageResult = postService.listByUser(userId, page, size, sort);
        return Result.success("success", pageResult);
    }

    @GetMapping("/{post_id}")
    @Operation(summary = "获取动态详情")
    public Result getPostDetail(@PathVariable("post_id") @Min(1) Long postId) {
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
    @Operation(summary = "更新动态")
    public Result updatePost(@RequestBody @Valid PostUpdateDTO postUpdateDTO, @PathVariable("post_id") @Min(1) Long postId) {
        log.info("updatePost postUpdateDTO={}, postId={}", postUpdateDTO, postId);
        postService.updatePost(postUpdateDTO,postId);
        return Result.success("更新成功");
    }




}
