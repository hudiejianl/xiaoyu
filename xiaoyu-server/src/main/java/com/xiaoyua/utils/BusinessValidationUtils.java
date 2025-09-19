package com.xiaoyua.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务验证工具类
 * 提供校遇平台特定的业务规则验证
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Slf4j
public class BusinessValidationUtils {
    
    /**
     * 私有构造函数，防止实例化
     */
    private BusinessValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // ========== 用户相关验证 ==========
    
    /**
     * 验证用户年龄是否符合平台要求
     * 校园社交平台要求用户年龄在16-35岁之间
     * 
     * @param birthday 生日
     * @return 是否符合要求
     */
    public static boolean isValidUserAge(LocalDate birthday) {
        return ValidationUtils.isValidAge(birthday, 16, 35);
    }
    
    /**
     * 验证昵称是否符合平台规范
     * 不能包含敏感词，长度2-30位
     * 
     * @param nickname 昵称
     * @param sensitiveWords 敏感词列表
     * @return 是否符合规范
     */
    public static boolean isValidNickname(String nickname, List<String> sensitiveWords) {
        if (!ValidationUtils.isValidNickname(nickname)) {
            return false;
        }
        
        return !ValidationUtils.containsSensitiveWords(nickname, sensitiveWords);
    }
    
    /**
     * 验证用户隐私设置值
     * 0-公开，1-好友可见，2-仅自己可见
     * 
     * @param privacyValue 隐私设置值
     * @return 是否有效
     */
    public static boolean isValidPrivacySetting(Integer privacyValue) {
        return privacyValue != null && privacyValue >= 0 && privacyValue <= 2;
    }
    
    // ========== 动态相关验证 ==========
    
    /**
     * 验证动态内容是否符合规范
     * 不能为空，不能超过2000字符，不能包含敏感词
     * 
     * @param content 动态内容
     * @param sensitiveWords 敏感词列表
     * @return 是否符合规范
     */
    public static boolean isValidPostContent(String content, List<String> sensitiveWords) {
        if (StrUtil.isEmpty(content) || content.length() > 2000) {
            return false;
        }
        
        return !ValidationUtils.containsSensitiveWords(content, sensitiveWords);
    }
    
    /**
     * 验证动态可见性设置
     * PUBLIC-公开，FRIEND-好友可见，CAMPUS-校园可见
     * 
     * @param visibility 可见性设置
     * @return 是否有效
     */
    public static boolean isValidPostVisibility(String visibility) {
        return StrUtil.isNotEmpty(visibility) && 
               ("PUBLIC".equals(visibility) || "FRIEND".equals(visibility) || "CAMPUS".equals(visibility));
    }
    
    /**
     * 验证地理位置坐标
     * 
     * @param latitude 纬度
     * @param longitude 经度
     * @return 是否有效
     */
    public static boolean isValidLocation(BigDecimal latitude, BigDecimal longitude) {
        if (latitude == null || longitude == null) {
            return false;
        }
        
        // 纬度范围：-90 到 90
        if (latitude.compareTo(new BigDecimal("-90")) < 0 || latitude.compareTo(new BigDecimal("90")) > 0) {
            return false;
        }
        
        // 经度范围：-180 到 180
        return longitude.compareTo(new BigDecimal("-180")) >= 0 && longitude.compareTo(new BigDecimal("180")) <= 0;
    }
    
    // ========== 任务相关验证 ==========
    
    /**
     * 验证任务悬赏金额
     * 最小0.01元，最大10000元
     * 
     * @param reward 悬赏金额
     * @return 是否有效
     */
    public static boolean isValidTaskReward(BigDecimal reward) {
        return ValidationUtils.isValidAmount(reward, new BigDecimal("0.01"), new BigDecimal("10000"));
    }
    
    /**
     * 验证任务截止时间
     * 必须是未来时间，且不能超过1年
     * 
     * @param expireAt 截止时间
     * @return 是否有效
     */
    public static boolean isValidTaskExpireTime(LocalDateTime expireAt) {
        if (expireAt == null) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxExpire = now.plusYears(1);
        
        return expireAt.isAfter(now) && expireAt.isBefore(maxExpire);
    }
    
    /**
     * 验证任务状态
     * 
     * @param status 任务状态
     * @return 是否有效
     */
    public static boolean isValidTaskStatus(String status) {
        if (StrUtil.isEmpty(status)) {
            return false;
        }
        
        List<String> validStatuses = List.of(
            "DRAFT", "AUDITING", "RECRUIT", "RUNNING", 
            "DELIVER", "FINISH", "CLOSED", "ARBITRATED"
        );
        
        return validStatuses.contains(status);
    }
    
    /**
     * 验证任务评分
     * 1-5分
     * 
     * @param score 评分
     * @return 是否有效
     */
    public static boolean isValidTaskScore(Integer score) {
        return score != null && score >= 1 && score <= 5;
    }
    
    // ========== 评论相关验证 ==========
    
    /**
     * 验证评论内容
     * 不能为空，不能超过500字符，不能包含敏感词
     * 
     * @param content 评论内容
     * @param sensitiveWords 敏感词列表
     * @return 是否符合规范
     */
    public static boolean isValidCommentContent(String content, List<String> sensitiveWords) {
        if (StrUtil.isEmpty(content) || content.length() > 500) {
            return false;
        }
        
        return !ValidationUtils.containsSensitiveWords(content, sensitiveWords);
    }
    
    // ========== 文件相关验证 ==========
    
    /**
     * 验证图片文件
     * 
     * @param filename 文件名
     * @param fileSize 文件大小（字节）
     * @return 是否有效
     */
    public static boolean isValidImageFile(String filename, long fileSize) {
        // 允许的图片格式
        String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "webp"};
        
        // 最大5MB
        long maxSize = 5 * 1024 * 1024;
        
        return ValidationUtils.isValidFileExtension(filename, allowedExtensions) &&
               ValidationUtils.isValidFileSize(fileSize, maxSize);
    }
    
    /**
     * 验证视频文件
     * 
     * @param filename 文件名
     * @param fileSize 文件大小（字节）
     * @return 是否有效
     */
    public static boolean isValidVideoFile(String filename, long fileSize) {
        // 允许的视频格式
        String[] allowedExtensions = {"mp4", "avi", "mov", "wmv", "flv", "webm"};
        
        // 最大100MB
        long maxSize = 100 * 1024 * 1024;
        
        return ValidationUtils.isValidFileExtension(filename, allowedExtensions) &&
               ValidationUtils.isValidFileSize(fileSize, maxSize);
    }
    
    /**
     * 验证文档文件
     * 
     * @param filename 文件名
     * @param fileSize 文件大小（字节）
     * @return 是否有效
     */
    public static boolean isValidDocumentFile(String filename, long fileSize) {
        // 允许的文档格式
        String[] allowedExtensions = {"pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt"};
        
        // 最大20MB
        long maxSize = 20 * 1024 * 1024;
        
        return ValidationUtils.isValidFileExtension(filename, allowedExtensions) &&
               ValidationUtils.isValidFileSize(fileSize, maxSize);
    }
    
    // ========== 搜索相关验证 ==========
    
    /**
     * 验证搜索关键词
     * 不能为空，长度1-50字符，不能包含特殊字符
     * 
     * @param keyword 搜索关键词
     * @return 是否有效
     */
    public static boolean isValidSearchKeyword(String keyword) {
        if (StrUtil.isEmpty(keyword) || keyword.length() > 50) {
            return false;
        }
        
        // 不能包含特殊字符
        String pattern = "^[\\u4e00-\\u9fa5a-zA-Z0-9\\s]+$";
        return keyword.matches(pattern);
    }
    
    /**
     * 验证搜索类型
     * 
     * @param searchType 搜索类型
     * @return 是否有效
     */
    public static boolean isValidSearchType(String searchType) {
        if (StrUtil.isEmpty(searchType)) {
            return false;
        }
        
        List<String> validTypes = List.of("ALL", "USER", "POST", "TASK", "TOPIC", "TAG");
        return validTypes.contains(searchType);
    }
    
    // ========== 通知相关验证 ==========
    
    /**
     * 验证通知类型
     * 
     * @param notificationType 通知类型
     * @return 是否有效
     */
    public static boolean isValidNotificationType(String notificationType) {
        if (StrUtil.isEmpty(notificationType)) {
            return false;
        }
        
        List<String> validTypes = List.of(
            "LIKE", "COMMENT", "FOLLOW", "FRIEND_REQUEST", 
            "TASK_ORDER", "TASK_REVIEW", "SYSTEM"
        );
        
        return validTypes.contains(notificationType);
    }
    
    // ========== 综合验证方法 ==========
    
    /**
     * 批量验证并收集错误信息
     * 
     * @param validations 验证规则列表
     * @return 错误信息列表
     */
    public static List<String> validateAndCollectErrors(List<ValidationRule> validations) {
        List<String> errors = new ArrayList<>();
        
        if (CollUtil.isEmpty(validations)) {
            return errors;
        }
        
        for (ValidationRule rule : validations) {
            if (!rule.isValid()) {
                errors.add(rule.getErrorMessage());
            }
        }
        
        return errors;
    }
    
    /**
     * 验证规则接口
     */
    public interface ValidationRule {
        /**
         * 验证是否通过
         * 
         * @return 是否通过验证
         */
        boolean isValid();
        
        /**
         * 获取错误消息
         * 
         * @return 错误消息
         */
        String getErrorMessage();
    }
    
    /**
     * 简单验证规则实现
     */
    public static class SimpleValidationRule implements ValidationRule {
        private final boolean valid;
        private final String errorMessage;
        
        public SimpleValidationRule(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }
        
        @Override
        public boolean isValid() {
            return valid;
        }
        
        @Override
        public String getErrorMessage() {
            return errorMessage;
        }
    }
    
    // ========== 校区相关验证 ==========
    
    /**
     * 验证校区ID是否有效
     * 
     * @param campusId 校区ID
     * @return 是否有效
     */
    public static boolean isValidCampusId(Long campusId) {
        return campusId != null && campusId > 0;
    }
    
    // ========== 好友相关验证 ==========
    
    /**
     * 验证好友状态
     * 
     * @param status 好友状态
     * @return 是否有效
     */
    public static boolean isValidFriendshipStatus(String status) {
        if (StrUtil.isEmpty(status)) {
            return false;
        }
        
        List<String> validStatuses = List.of("PENDING", "ACCEPTED", "REFUSED");
        return validStatuses.contains(status);
    }
    
    /**
     * 验证私信内容
     * 不能为空，不能超过1000字符，不能包含敏感词
     * 
     * @param content 私信内容
     * @param sensitiveWords 敏感词列表
     * @return 是否符合规范
     */
    public static boolean isValidMessageContent(String content, List<String> sensitiveWords) {
        if (StrUtil.isEmpty(content) || content.length() > 1000) {
            return false;
        }
        
        return !ValidationUtils.containsSensitiveWords(content, sensitiveWords);
    }
    
    // ========== 举报相关验证 ==========
    
    /**
     * 验证举报类型
     * 
     * @param itemType 举报类型
     * @return 是否有效
     */
    public static boolean isValidReportItemType(String itemType) {
        if (StrUtil.isEmpty(itemType)) {
            return false;
        }
        
        List<String> validTypes = List.of("POST", "TASK", "COMMENT", "USER");
        return validTypes.contains(itemType);
    }
    
    /**
     * 验证举报状态
     * 
     * @param status 举报状态
     * @return 是否有效
     */
    public static boolean isValidReportStatus(String status) {
        if (StrUtil.isEmpty(status)) {
            return false;
        }
        
        List<String> validStatuses = List.of("PENDING", "ACCEPTED", "REFUSED");
        return validStatuses.contains(status);
    }
    
    /**
     * 验证举报理由
     * 不能为空，不能超过255字符
     * 
     * @param reason 举报理由
     * @return 是否符合规范
     */
    public static boolean isValidReportReason(String reason) {
        return StrUtil.isNotEmpty(reason) && reason.length() <= 255;
    }
    
    // ========== 话题相关验证 ==========
    
    /**
     * 验证话题名称
     * 必须以#开头和结尾，长度3-50字符
     * 
     * @param topicName 话题名称
     * @return 是否有效
     */
    public static boolean isValidTopicName(String topicName) {
        if (StrUtil.isEmpty(topicName)) {
            return false;
        }
        
        // 话题名称必须以#开头和结尾
        if (!topicName.startsWith("#") || !topicName.endsWith("#")) {
            return false;
        }
        
        // 去掉#后的内容长度应该在1-48字符之间
        String content = topicName.substring(1, topicName.length() - 1);
        return content.length() >= 1 && content.length() <= 48;
    }
    
    /**
     * 验证话题描述
     * 不能超过500字符
     * 
     * @param description 话题描述
     * @return 是否有效
     */
    public static boolean isValidTopicDescription(String description) {
        return description == null || description.length() <= 500;
    }
    
    // ========== 标签相关验证 ==========
    
    /**
     * 验证标签名称
     * 不能为空，长度1-30字符
     * 
     * @param tagName 标签名称
     * @return 是否有效
     */
    public static boolean isValidTagName(String tagName) {
        return StrUtil.isNotEmpty(tagName) && tagName.length() <= 30;
    }
    
    /**
     * 验证标签类别
     * 
     * @param category 标签类别
     * @return 是否有效
     */
    public static boolean isValidTagCategory(String category) {
        if (StrUtil.isEmpty(category)) {
            return false;
        }
        
        List<String> validCategories = List.of("TASK", "POST");
        return validCategories.contains(category);
    }
    
    // ========== 系统公告相关验证 ==========
    
    /**
     * 验证公告标题
     * 不能为空，不能超过100字符
     * 
     * @param title 公告标题
     * @return 是否有效
     */
    public static boolean isValidNoticeTitle(String title) {
        return StrUtil.isNotEmpty(title) && title.length() <= 100;
    }
    
    /**
     * 验证公告内容
     * 不能为空，不能超过5000字符
     * 
     * @param content 公告内容
     * @return 是否有效
     */
    public static boolean isValidNoticeContent(String content) {
        return StrUtil.isNotEmpty(content) && content.length() <= 5000;
    }
    
    /**
     * 验证公告时间范围
     * 开始时间不能晚于结束时间
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 是否有效
     */
    public static boolean isValidNoticeTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            return false;
        }
        
        return startTime.isBefore(endTime) || startTime.isEqual(endTime);
    }
    
    // ========== 敏感词相关验证 ==========
    
    /**
     * 验证敏感词等级
     * 1-低危，2-中危，3-高危
     * 
     * @param level 敏感词等级
     * @return 是否有效
     */
    public static boolean isValidSensitiveWordLevel(Integer level) {
        return level != null && level >= 1 && level <= 3;
    }
    
    /**
     * 验证敏感词内容
     * 不能为空，不能超过50字符
     * 
     * @param word 敏感词
     * @return 是否有效
     */
    public static boolean isValidSensitiveWord(String word) {
        return StrUtil.isNotEmpty(word) && word.length() <= 50;
    }
    
    // ========== 分页参数验证 ==========
    
    /**
     * 验证分页参数
     * 
     * @param page 页码
     * @param size 每页大小
     * @return 是否有效
     */
    public static boolean isValidPageParams(Integer page, Integer size) {
        return page != null && page > 0 && size != null && size > 0 && size <= 100;
    }
    
    /**
     * 验证排序参数
     * 
     * @param sort 排序字段
     * @param allowedFields 允许的排序字段
     * @return 是否有效
     */
    public static boolean isValidSortField(String sort, List<String> allowedFields) {
        if (StrUtil.isEmpty(sort) || CollUtil.isEmpty(allowedFields)) {
            return false;
        }
        
        // 支持字段名和字段名+排序方向的格式
        String field = sort;
        if (sort.contains(" ")) {
            String[] parts = sort.split(" ");
            field = parts[0];
            String direction = parts[1].toUpperCase();
            if (!"ASC".equals(direction) && !"DESC".equals(direction)) {
                return false;
            }
        }
        
        return allowedFields.contains(field);
    }
    
    // ========== 业务状态验证 ==========
    
    /**
     * 验证用户状态
     * 
     * @param status 用户状态
     * @return 是否有效
     */
    public static boolean isValidUserStatus(Integer status) {
        return status != null && (status == 0 || status == 1);
    }
    
    /**
     * 验证性别
     * 
     * @param gender 性别
     * @return 是否有效
     */
    public static boolean isValidGender(Integer gender) {
        return gender != null && gender >= 0 && gender <= 2;
    }
    
    /**
     * 验证实名状态
     * 
     * @param isRealName 实名状态
     * @return 是否有效
     */
    public static boolean isValidRealNameStatus(Integer isRealName) {
        return isRealName != null && (isRealName == 0 || isRealName == 1);
    }
    
    // ========== 业务规则组合验证 ==========
    
    /**
     * 验证用户是否可以发布动态
     * 
     * @param userStatus 用户状态
     * @param isRealName 是否实名
     * @return 是否可以发布
     */
    public static boolean canPublishPost(Integer userStatus, Integer isRealName) {
        return isValidUserStatus(userStatus) && userStatus == 0 && 
               isValidRealNameStatus(isRealName) && isRealName == 1;
    }
    
    /**
     * 验证用户是否可以发布任务
     * 
     * @param userStatus 用户状态
     * @param isRealName 是否实名
     * @return 是否可以发布
     */
    public static boolean canPublishTask(Integer userStatus, Integer isRealName) {
        return isValidUserStatus(userStatus) && userStatus == 0 && 
               isValidRealNameStatus(isRealName) && isRealName == 1;
    }
    
    /**
     * 验证用户是否可以接取任务
     * 
     * @param userStatus 用户状态
     * @param isRealName 是否实名
     * @return 是否可以接取
     */
    public static boolean canReceiveTask(Integer userStatus, Integer isRealName) {
        return isValidUserStatus(userStatus) && userStatus == 0 && 
               isValidRealNameStatus(isRealName) && isRealName == 1;
    }
    
    /**
     * 验证任务是否可以被接取
     * 
     * @param taskStatus 任务状态
     * @param expireAt 截止时间
     * @return 是否可以接取
     */
    public static boolean canTaskBeReceived(String taskStatus, LocalDateTime expireAt) {
        if (!isValidTaskStatus(taskStatus) || !"RECRUIT".equals(taskStatus)) {
            return false;
        }
        
        if (expireAt != null && expireAt.isBefore(LocalDateTime.now())) {
            return false;
        }
        
        return true;
    }
    
    // ========== 批量验证辅助方法 ==========
    
    /**
     * 创建简单验证规则
     * 
     * @param condition 验证条件
     * @param errorMessage 错误消息
     * @return 验证规则
     */
    public static ValidationRule createRule(boolean condition, String errorMessage) {
        return new SimpleValidationRule(condition, errorMessage);
    }
    
    /**
     * 验证用户基本信息
     * 
     * @param nickname 昵称
     * @param birthday 生日
     * @param gender 性别
     * @param sensitiveWords 敏感词列表
     * @return 验证错误列表
     */
    public static List<String> validateUserBasicInfo(String nickname, LocalDate birthday, 
                                                   Integer gender, List<String> sensitiveWords) {
        List<ValidationRule> rules = List.of(
            createRule(isValidNickname(nickname, sensitiveWords), "昵称格式不正确或包含敏感词"),
            createRule(birthday == null || isValidUserAge(birthday), "年龄不符合平台要求"),
            createRule(isValidGender(gender), "性别参数无效")
        );
        
        return validateAndCollectErrors(rules);
    }
    
    /**
     * 验证动态发布信息
     * 
     * @param content 动态内容
     * @param visibility 可见性
     * @param latitude 纬度
     * @param longitude 经度
     * @param sensitiveWords 敏感词列表
     * @return 验证错误列表
     */
    public static List<String> validatePostInfo(String content, String visibility, 
                                               BigDecimal latitude, BigDecimal longitude, 
                                               List<String> sensitiveWords) {
        List<ValidationRule> rules = List.of(
            createRule(isValidPostContent(content, sensitiveWords), "动态内容不符合规范"),
            createRule(isValidPostVisibility(visibility), "可见性设置无效"),
            createRule(latitude == null || longitude == null || isValidLocation(latitude, longitude), 
                      "地理位置坐标无效")
        );
        
        return validateAndCollectErrors(rules);
    }
    
    /**
     * 验证任务发布信息
     * 
     * @param title 任务标题
     * @param content 任务内容
     * @param reward 悬赏金额
     * @param expireAt 截止时间
     * @param sensitiveWords 敏感词列表
     * @return 验证错误列表
     */
    public static List<String> validateTaskInfo(String title, String content, BigDecimal reward, 
                                               LocalDateTime expireAt, List<String> sensitiveWords) {
        List<ValidationRule> rules = List.of(
            createRule(StrUtil.isNotEmpty(title) && title.length() <= 100 && 
                      !ValidationUtils.containsSensitiveWords(title, sensitiveWords), 
                      "任务标题不符合规范"),
            createRule(isValidPostContent(content, sensitiveWords), "任务内容不符合规范"),
            createRule(isValidTaskReward(reward), "悬赏金额无效"),
            createRule(expireAt == null || isValidTaskExpireTime(expireAt), "截止时间无效")
        );
        
        return validateAndCollectErrors(rules);
    }
}