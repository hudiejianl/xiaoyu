package com.xiaoyua.vo.admin;

/**
 * 敏感词信息VO
 * 用于返回敏感词的详细信息
 * 
 * @author xiaoyu
 */
public class SensitiveWordVO {
    
    /**
     * 敏感词内容
     */
    private String word;
    
    /**
     * 危险等级：1-低，2-中，3-高
     */
    private Integer level;
    
    /**
     * 危险等级描述
     */
    private String levelDesc;
    
    public SensitiveWordVO() {}
    
    public SensitiveWordVO(String word, Integer level) {
        this.word = word;
        this.level = level;
        this.levelDesc = getLevelDescription(level);
    }
    
    public SensitiveWordVO(String word, Integer level, String levelDesc) {
        this.word = word;
        this.level = level;
        this.levelDesc = levelDesc;
    }
    
    /**
     * 根据等级获取描述
     */
    private String getLevelDescription(Integer level) {
        if (level == null) {
            return "未知";
        }
        switch (level) {
            case 1:
                return "低";
            case 2:
                return "中";
            case 3:
                return "高";
            default:
                return "未知";
        }
    }
    
    public String getWord() {
        return word;
    }
    
    public void setWord(String word) {
        this.word = word;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
        this.levelDesc = getLevelDescription(level);
    }
    
    public String getLevelDesc() {
        return levelDesc;
    }
    
    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }
    
    @Override
    public String toString() {
        return "SensitiveWordVO{" +
                "word='" + word + '\'' +
                ", level=" + level +
                ", levelDesc='" + levelDesc + '\'' +
                '}';
    }
}