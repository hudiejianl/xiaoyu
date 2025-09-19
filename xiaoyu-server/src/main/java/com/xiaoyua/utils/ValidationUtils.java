package com.xiaoyua.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 数据验证工具类
 * 提供各种业务规则验证和数据格式验证功能
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Slf4j
public class ValidationUtils {

    /**
     * JSR-303 验证器
     */
    private static final Validator VALIDATOR;

    /**
     * 常用正则表达式
     */
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    private static final Pattern PASSWORD_PATTERN = Pattern
            .compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,20}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{4,20}$");
    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[\\u4e00-\\u9fa5a-zA-Z0-9_]{2,30}$");
    private static final Pattern VERIFICATION_CODE_PATTERN = Pattern.compile("^\\d{6}$");

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        VALIDATOR = factory.getValidator();
    }

    /**
     * 私有构造函数，防止实例化
     */
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ========== JSR-303 验证方法 ==========

    /**
     * 验证对象
     * 
     * @param object 要验证的对象
     * @param <T>    对象类型
     * @return 验证结果，无错误时返回空集合
     */
    public static <T> Set<ConstraintViolation<T>> validate(T object) {
        return VALIDATOR.validate(object);
    }

    /**
     * 验证对象（指定分组）
     * 
     * @param object 要验证的对象
     * @param groups 验证分组
     * @param <T>    对象类型
     * @return 验证结果，无错误时返回空集合
     */
    public static <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return VALIDATOR.validate(object, groups);
    }

    /**
     * 验证对象属性
     * 
     * @param object       要验证的对象
     * @param propertyName 属性名
     * @param <T>          对象类型
     * @return 验证结果，无错误时返回空集合
     */
    public static <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName) {
        return VALIDATOR.validateProperty(object, propertyName);
    }

    /**
     * 验证属性值
     * 
     * @param beanType     Bean类型
     * @param propertyName 属性名
     * @param value        属性值
     * @param <T>          Bean类型
     * @return 验证结果，无错误时返回空集合
     */
    public static <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value) {
        return VALIDATOR.validateValue(beanType, propertyName, value);
    }

    /**
     * 检查对象是否通过验证
     * 
     * @param object 要验证的对象
     * @param <T>    对象类型
     * @return 是否通过验证
     */
    public static <T> boolean isValid(T object) {
        return validate(object).isEmpty();
    }

    /**
     * 获取验证错误消息列表
     * 
     * @param violations 验证结果
     * @param <T>        对象类型
     * @return 错误消息列表
     */
    public static <T> List<String> getErrorMessages(Set<ConstraintViolation<T>> violations) {
        if (CollUtil.isEmpty(violations)) {
            return new ArrayList<>();
        }

        List<String> messages = new ArrayList<>();
        for (ConstraintViolation<T> violation : violations) {
            messages.add(violation.getMessage());
        }
        return messages;
    }

    /**
     * 获取验证错误消息映射
     * 
     * @param violations 验证结果
     * @param <T>        对象类型
     * @return 错误消息映射（字段名 -> 错误消息）
     */
    public static <T> Map<String, String> getErrorMessageMap(Set<ConstraintViolation<T>> violations) {
        if (CollUtil.isEmpty(violations)) {
            return new HashMap<>();
        }

        Map<String, String> messageMap = new HashMap<>();
        for (ConstraintViolation<T> violation : violations) {
            String propertyPath = violation.getPropertyPath().toString();
            messageMap.put(propertyPath, violation.getMessage());
        }
        return messageMap;
    }

    // ========== 格式验证方法 ==========

    /**
     * 验证手机号格式
     * 
     * @param mobile 手机号
     * @return 是否有效
     */
    public static boolean isValidMobile(String mobile) {
        return StrUtil.isNotEmpty(mobile) && MOBILE_PATTERN.matcher(mobile).matches();
    }

    /**
     * 验证邮箱格式
     * 
     * @param email 邮箱
     * @return 是否有效
     */
    public static boolean isValidEmail(String email) {
        return StrUtil.isNotEmpty(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证身份证号格式
     * 
     * @param idCard 身份证号
     * @return 是否有效
     */
    public static boolean isValidIdCard(String idCard) {
        return StrUtil.isNotEmpty(idCard) && IdcardUtil.isValidCard(idCard);
    }

    /**
     * 验证密码强度
     * 要求：8-20位，包含大小写字母和数字
     * 
     * @param password 密码
     * @return 是否有效
     */
    public static boolean isValidPassword(String password) {
        return StrUtil.isNotEmpty(password) && PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * 验证用户名格式
     * 要求：4-20位，只能包含字母、数字、下划线
     * 
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean isValidUsername(String username) {
        return StrUtil.isNotEmpty(username) && USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * 验证昵称格式
     * 要求：2-30位，可包含中文、字母、数字、下划线
     * 
     * @param nickname 昵称
     * @return 是否有效
     */
    public static boolean isValidNickname(String nickname) {
        return StrUtil.isNotEmpty(nickname) && NICKNAME_PATTERN.matcher(nickname).matches();
    }

    /**
     * 验证验证码格式
     * 要求：6位数字
     * 
     * @param code 验证码
     * @return 是否有效
     */
    public static boolean isValidVerificationCode(String code) {
        return StrUtil.isNotEmpty(code) && VERIFICATION_CODE_PATTERN.matcher(code).matches();
    }

    // ========== 业务规则验证方法 ==========

    /**
     * 验证年龄范围
     * 
     * @param birthday 生日
     * @param minAge   最小年龄
     * @param maxAge   最大年龄
     * @return 是否在有效范围内
     */
    public static boolean isValidAge(LocalDate birthday, int minAge, int maxAge) {
        if (birthday == null) {
            return false;
        }

        LocalDate now = LocalDate.now();
        int age = now.getYear() - birthday.getYear();

        // 调整年龄计算
        if (now.getDayOfYear() < birthday.getDayOfYear()) {
            age--;
        }

        return age >= minAge && age <= maxAge;
    }

    /**
     * 验证金额范围
     * 
     * @param amount 金额
     * @param min    最小值
     * @param max    最大值
     * @return 是否在有效范围内
     */
    public static boolean isValidAmount(BigDecimal amount, BigDecimal min, BigDecimal max) {
        if (amount == null) {
            return false;
        }

        return amount.compareTo(min) >= 0 && amount.compareTo(max) <= 0;
    }

    /**
     * 验证时间范围
     * 
     * @param dateTime 时间
     * @param start    开始时间
     * @param end      结束时间
     * @return 是否在有效范围内
     */
    public static boolean isValidDateTimeRange(LocalDateTime dateTime, LocalDateTime start, LocalDateTime end) {
        if (dateTime == null) {
            return false;
        }

        return (start == null || dateTime.isAfter(start) || dateTime.isEqual(start)) &&
                (end == null || dateTime.isBefore(end) || dateTime.isEqual(end));
    }

    /**
     * 验证文件扩展名
     * 
     * @param filename          文件名
     * @param allowedExtensions 允许的扩展名
     * @return 是否有效
     */
    public static boolean isValidFileExtension(String filename, String... allowedExtensions) {
        if (StrUtil.isEmpty(filename) || allowedExtensions == null || allowedExtensions.length == 0) {
            return false;
        }

        String extension = StrUtil.subAfter(filename, ".", true);
        if (StrUtil.isEmpty(extension)) {
            return false;
        }

        for (String allowed : allowedExtensions) {
            if (extension.equalsIgnoreCase(allowed)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证文件大小
     * 
     * @param fileSize 文件大小（字节）
     * @param maxSize  最大大小（字节）
     * @return 是否有效
     */
    public static boolean isValidFileSize(long fileSize, long maxSize) {
        return fileSize > 0 && fileSize <= maxSize;
    }

    /**
     * 验证IP地址格式
     * 
     * @param ip IP地址
     * @return 是否有效
     */
    public static boolean isValidIpAddress(String ip) {
        if (StrUtil.isEmpty(ip)) {
            return false;
        }

        // IPv4 格式验证
        String ipv4Pattern = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
        return ReUtil.isMatch(ipv4Pattern, ip);
    }

    /**
     * 验证URL格式
     * 
     * @param url URL地址
     * @return 是否有效
     */
    public static boolean isValidUrl(String url) {
        if (StrUtil.isEmpty(url)) {
            return false;
        }

        String urlPattern = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        return ReUtil.isMatch(urlPattern, url);
    }

    // ========== 敏感词验证方法 ==========

    /**
     * 检查文本是否包含敏感词
     * 
     * @param text           文本内容
     * @param sensitiveWords 敏感词列表
     * @return 是否包含敏感词
     */
    public static boolean containsSensitiveWords(String text, List<String> sensitiveWords) {
        if (StrUtil.isEmpty(text) || CollUtil.isEmpty(sensitiveWords)) {
            return false;
        }

        String lowerText = text.toLowerCase();
        for (String word : sensitiveWords) {
            if (StrUtil.isNotEmpty(word) && lowerText.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文本中的敏感词
     * 
     * @param text           文本内容
     * @param sensitiveWords 敏感词列表
     * @return 找到的敏感词列表
     */
    public static List<String> findSensitiveWords(String text, List<String> sensitiveWords) {
        List<String> foundWords = new ArrayList<>();
        if (StrUtil.isEmpty(text) || CollUtil.isEmpty(sensitiveWords)) {
            return foundWords;
        }

        String lowerText = text.toLowerCase();
        for (String word : sensitiveWords) {
            if (StrUtil.isNotEmpty(word) && lowerText.contains(word.toLowerCase())) {
                foundWords.add(word);
            }
        }
        return foundWords;
    }

    // ========== 工具方法 ==========

    /**
     * 清理和标准化手机号
     * 
     * @param mobile 原始手机号
     * @return 标准化后的手机号
     */
    public static String normalizeMobile(String mobile) {
        if (StrUtil.isEmpty(mobile)) {
            return mobile;
        }

        // 移除所有非数字字符
        String cleaned = ReUtil.replaceAll(mobile, "[^\\d]", "");

        // 如果以+86开头，移除+86
        if (cleaned.startsWith("86") && cleaned.length() == 13) {
            cleaned = cleaned.substring(2);
        }

        return cleaned;
    }

    /**
     * 脱敏手机号
     * 
     * @param mobile 手机号
     * @return 脱敏后的手机号
     */
    public static String maskMobile(String mobile) {
        if (!isValidMobile(mobile)) {
            return mobile;
        }

        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    /**
     * 脱敏身份证号
     * 
     * @param idCard 身份证号
     * @return 脱敏后的身份证号
     */
    public static String maskIdCard(String idCard) {
        if (!isValidIdCard(idCard)) {
            return idCard;
        }

        return idCard.substring(0, 6) + "********" + idCard.substring(14);
    }

    /**
     * 脱敏邮箱
     * 
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    public static String maskEmail(String email) {
        if (!isValidEmail(email)) {
            return email;
        }

        int atIndex = email.indexOf("@");
        if (atIndex <= 1) {
            return email;
        }

        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if (username.length() <= 3) {
            return username.charAt(0) + "***" + domain;
        } else {
            return username.substring(0, 2) + "***" + username.substring(username.length() - 1) + domain;
        }
    }
}