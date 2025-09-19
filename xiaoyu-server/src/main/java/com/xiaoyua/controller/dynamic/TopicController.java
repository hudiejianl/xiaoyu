package com.xiaoyua.controller.dynamic;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyua.result.Result;
import com.xiaoyua.service.TopicService;
import com.xiaoyua.vo.post.PostVO;
import com.xiaoyua.vo.topic.TopicSimpleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/topics")
@Slf4j
@Tag(name = "话题管理", description = "话题相关接口")
@Validated
public class TopicController {

    @Autowired
    private TopicService topicService;

    /**
     * 11.3 获取热门话题
     */
    @GetMapping("/hot")
    @Operation(summary = "获取热门话题")
    public Result<List<TopicSimpleVO>> getHotTopics(
            @RequestParam(defaultValue = "10") @Min(1) Integer limit) {
        log.info("获取热门话题列表，limit={}", limit);
        List<TopicSimpleVO> topics = topicService.getHotTopics(limit);
        return Result.success("success", topics);
    }

    /**
     * 11.x 获取所有话题
     */
    @GetMapping
    @Operation(summary = "获取所有话题")
    public Result<List<TopicSimpleVO>> getAllTopics() {
        log.info("获取所有话题列表");
        List<TopicSimpleVO> topics = topicService.getAllTopics();
        return Result.success("success", topics);
    }

    /**
     * 11.4 获取话题详情
     */
    @GetMapping("/{topic_id}")
    @Operation(summary = "获取话题详情", description = "获取指定话题的详细信息")
    public Result<TopicSimpleVO> getTopicById(
            @Parameter(description = "话题ID") @PathVariable("topic_id") Long topicId) {
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
            @Parameter(description = "页码，默认1") @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @Parameter(description = "每页数量，默认20") @RequestParam(defaultValue = "20") @Min(1) Integer size,
            @Parameter(description = "排序：hot/latest") @RequestParam(required = false) String sort) {
        log.info("获取话题下的动态列表，topicId={}, page={}, size={}, sort={}", topicId, page, size, sort);
        IPage<PostVO> posts = topicService.getPostsByTopicId(topicId, page, size, sort);
        return Result.success("success", posts);
    }
}
