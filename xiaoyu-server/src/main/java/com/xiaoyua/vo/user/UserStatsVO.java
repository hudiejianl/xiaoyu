package com.xiaoyua.vo.user;

/**
 * 用户统计信息VO
 * 用于返回用户的统计数据
 * 
 * @author xiaoyu
 */
public class UserStatsVO {
    
    /**
     * 发布动态数量
     */
    private Integer postCount;
    
    /**
     * 粉丝数量
     */
    private Integer followerCount;
    
    /**
     * 关注数量
     */
    private Integer followingCount;
    
    public UserStatsVO() {}
    
    public UserStatsVO(Integer postCount, Integer followerCount, Integer followingCount) {
        this.postCount = postCount;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }
    
    public Integer getPostCount() {
        return postCount;
    }
    
    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }
    
    public Integer getFollowerCount() {
        return followerCount;
    }
    
    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }
    
    public Integer getFollowingCount() {
        return followingCount;
    }
    
    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }
    
    @Override
    public String toString() {
        return "UserStatsVO{" +
                "postCount=" + postCount +
                ", followerCount=" + followerCount +
                ", followingCount=" + followingCount +
                '}';
    }
}