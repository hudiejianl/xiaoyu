package com.xiaoyua.dto.admin;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 敏感词管理DTO
 * 用于接收敏感词的增删改操作请求数据
 * 
 * @author xiaoyu
 */
@Data
public class SensitiveWordDTO {
    
    /**
     * 敏感词内容
     */
    @NotBlank(message = "敏感词不能为空")
    @Size(max = 50, message = "敏感词最多50个字符")
    private String word;
    
    /**
     * 危险等级：1-低，2-中，3-高
     */
    @NotNull(message = "危险等级不能为空")
    @Min(value = 1, message = "危险等级必须在1-3之间")
    @Max(value = 3, message = "危险等级必须在1-3之间")
    private Integer level;
//
//    public SensitiveWordDTO() {}
//
//    public SensitiveWordDTO(String word, Integer level) {
//        this.word = word;
//        this.level = level;
//    }
//
//    public String getWord() {
//        return word;
//    }
//
//    public void setWord(String word) {
//        this.word = word;
//    }
//
//    public Integer getLevel() {
//        return level;
//    }
//
//    public void setLevel(Integer level) {
//        this.level = level;
//    }
//
//    @Override
//    public String toString() {
//        return "SensitiveWordDTO{" +
//                "word='" + word + '\'' +
//                ", level=" + level +
//                '}';
//    }
}