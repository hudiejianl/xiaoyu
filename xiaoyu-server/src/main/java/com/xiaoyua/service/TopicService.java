package com.xiaoyua.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyua.vo.post.PostVO;
import com.xiaoyua.vo.topic.TopicSimpleVO;

import java.util.List;

/**
 * 话题服务接口
 */
public interface TopicService {

    /**
     * 获取热门话题列表
     * @param limit 数量限制，默认10
     * @return 热门话题列表
     */
    List<TopicSimpleVO> getHotTopics(Integer limit);

    /**
     * 获取所有话题列表（不分页）
     */
    List<TopicSimpleVO> getAllTopics();

    /**
     * 根据话题ID获取话题详情
     * @param topicId 话题ID
     * @return 话题详情
     */
    TopicSimpleVO getTopicById(Long topicId);

    /**
     * 获取话题下的动态列表
     * @param topicId 话题ID
     * @param page 页码
     * @param size 每页数量
     * @param sort 排序方式：hot/latest
     * @return 动态列表
     */
    IPage<PostVO> getPostsByTopicId(Long topicId, Integer page, Integer size, String sort);
}
