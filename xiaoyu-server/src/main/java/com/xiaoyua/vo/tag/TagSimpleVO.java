package com.xiaoyua.vo.tag;

/**
 * 标签简要信息VO
 * 用于返回标签的基本信息
 * 
 * @author xiaoyu
 */
public class TagSimpleVO {
    
    /**
     * 标签ID
     */
    private Integer id;
    
    /**
     * 标签名称
     */
    private String name;
    
    /**
     * 标签分类
     */
    private String category;
    
    /**
     * 标签颜色
     */
    private String color;
    
    /**
     * 使用次数
     */
    private Integer useCount;
    
    public TagSimpleVO() {}
    
    public TagSimpleVO(Integer id, String name, String category, String color, Integer useCount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.color = color;
        this.useCount = useCount;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Integer getUseCount() {
        return useCount;
    }
    
    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }
    
    @Override
    public String toString() {
        return "TagSimpleVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", color='" + color + '\'' +
                ", useCount=" + useCount +
                '}';
    }
}