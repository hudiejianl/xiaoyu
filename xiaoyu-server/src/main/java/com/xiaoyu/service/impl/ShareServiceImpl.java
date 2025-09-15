package com.xiaoyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaoyu.entity.LikePO;
import com.xiaoyu.entity.SharePO;
import com.xiaoyu.mapper.ShareMapper;
import com.xiaoyu.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShareServiceImpl implements ShareService {
    @Autowired
    ShareMapper shareMapper;
    @Override
    public void addShare(Long postId, Long userId) {
        SharePO share=new SharePO();
        share.setUserId(userId);
        share.setItemId(postId);
        share.setItemType(SharePO.ItemType.POST);
        share.setCreatedAt(LocalDateTime.now());
        shareMapper.insert(share);
    }

    @Override
    public long getShareCount(Long postId,String type){
        return shareMapper.selectCount(
                new QueryWrapper<SharePO>()
                        .eq("item_id", postId)
                        .eq("item_type", type.toUpperCase())
        );
    }


}
