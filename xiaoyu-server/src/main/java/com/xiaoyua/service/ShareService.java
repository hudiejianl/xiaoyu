package com.xiaoyua.service;


public interface ShareService {
    public void addShare(Long postId,Long userId);

    public long getShareCount(Long postId,String type);
}
