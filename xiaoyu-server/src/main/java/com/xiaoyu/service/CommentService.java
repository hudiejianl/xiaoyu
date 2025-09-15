package com.xiaoyu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyu.dto.comment.CommentCreateDTO;
import com.xiaoyu.vo.comment.CommentVO;

import java.util.List;

public interface CommentService {
    public void addComment(CommentCreateDTO comment);

    public void deleteComment(Long commentId);

    public IPage<CommentVO> getComments(Long postId, int page, int size, String sort);
    public long getCommentCount(Long postId, String type);
}
