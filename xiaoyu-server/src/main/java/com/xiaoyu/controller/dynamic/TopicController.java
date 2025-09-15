package com.xiaoyu.controller.dynamic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyu.result.Result;
import com.xiaoyu.service.TopicService;
import com.xiaoyu.vo.post.PostVO;
import com.xiaoyu.vo.topic.TopicSimpleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@Slf4j
@Tag(name = "话题管理", description = "话题相关接口")
public class TopicController {

    @Autowired
    private TopicService topicService;

    /**
     * 11.3 获取热门话题
     */
    @GetMapping("/hot")
    @Operation(summary = "获取热门话题")
    public Result<List<TopicSimpleVO>> getHotTopics(
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        log.info("获取热门话题列表，limit={}", limit);
        List<TopicSimpleVO> topics = topicService.getHotTopics(limit);
        return Result.success(topics);
    }

    /**
     * 11.4 获取话题详情
     */
    @GetMapping("/{topic_id}")
    @Operation(summary = "获取话题详情", description = "获取指定话题的详细信息")
    public Result<TopicSimpleVO> getTopicById(
            @Parameter(description = "话题ID") @PathVariable("topic_id") Long topicId
    ) {
        log.info("获取话题详情，topicId={}", topicId);
        TopicSimpleVO topic = topicService.getTopicById(topicId);
        if (topic == null) {
            return Result.error("话题不存在");
        }
        return Result.success("success", topic);
    }

    /**
     * 11.5 获取话题下的动态列表
     */
    @GetMapping("/{topic_id}/posts")
    @Operation(summary = "获取话题下的动态列表", description = "获取指定话题下的动态列表")
    public Result<IPage<PostVO>> getPostsByTopicId(
            @Parameter(description = "话题ID") @PathVariable("topic_id") Long topicId,
            @Parameter(description = "页码，默认1") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量，默认20") @RequestParam(defaultValue = "20") Integer size,
            @Parameter(description = "排序：hot/latest") @RequestParam(required = false) String sort
    ) {
        log.info("获取话题下的动态列表，topicId={}, page={}, size={}, sort={}", topicId, page, size, sort);
        IPage<PostVO> posts = topicService.getPostsByTopicId(topicId, page, size, sort);
        return Result.success("success", posts);
    }
}
