package com.xiaoyua.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Bean 转换工具类
 * 提供 VO/DTO 与 PO 之间的转换功能，支持性能优化和缓存机制
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Slf4j
public class BeanConverter {
    
    /**
     * 转换器缓存，提高性能
     */
    private static final Map<String, Function<Object, Object>> CONVERTER_CACHE = new ConcurrentHashMap<>();
    
    /**
     * 私有构造函数，防止实例化
     */
    private BeanConverter() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // ========== 单对象转换方法 ==========
    
    /**
     * 将源对象转换为目标类型对象
     * 
     * @param source 源对象
     * @param targetClass 目标类型
     * @param <T> 目标类型泛型
     * @return 转换后的对象，源对象为null时返回null
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        
        try {
            return BeanUtil.copyProperties(source, targetClass);
        } catch (Exception e) {
            log.error("Bean conversion failed: {} -> {}", source.getClass().getSimpleName(), 
                     targetClass.getSimpleName(), e);
            return null;
        }
    }
    
    /**
     * 将源对象转换为目标类型对象（忽略指定属性）
     * 
     * @param source 源对象
     * @param targetClass 目标类型
     * @param ignoreProperties 要忽略的属性名数组
     * @param <T> 目标类型泛型
     * @return 转换后的对象，源对象为null时返回null
     */
    public static <T> T convert(Object source, Class<T> targetClass, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        
        try {
            return BeanUtil.copyProperties(source, targetClass, ignoreProperties);
        } catch (Exception e) {
            log.error("Bean conversion with ignore properties failed: {} -> {}", 
                     source.getClass().getSimpleName(), targetClass.getSimpleName(), e);
            return null;
        }
    }
    
    /**
     * 将源对象转换为目标对象（目标对象已存在）
     * 
     * @param source 源对象
     * @param target 目标对象
     * @param <T> 目标类型泛型
     * @return 转换后的目标对象，源对象为null时返回原目标对象
     */
    public static <T> T convert(Object source, T target) {
        if (source == null || target == null) {
            return target;
        }
        
        try {
            BeanUtil.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            log.error("Bean conversion to existing target failed: {} -> {}", 
                     source.getClass().getSimpleName(), target.getClass().getSimpleName(), e);
            return target;
        }
    }
    
    // ========== 集合转换方法 ==========
    
    /**
     * 将源对象列表转换为目标类型对象列表
     * 
     * @param sourceList 源对象列表
     * @param targetClass 目标类型
     * @param <S> 源类型泛型
     * @param <T> 目标类型泛型
     * @return 转换后的对象列表，源列表为null或空时返回空列表
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        
        return sourceList.stream()
                .filter(Objects::nonNull)
                .map(source -> convert(source, targetClass))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    /**
     * 将源对象列表转换为目标类型对象列表（忽略指定属性）
     * 
     * @param sourceList 源对象列表
     * @param targetClass 目标类型
     * @param ignoreProperties 要忽略的属性名数组
     * @param <S> 源类型泛型
     * @param <T> 目标类型泛型
     * @return 转换后的对象列表，源列表为null或空时返回空列表
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass, String... ignoreProperties) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        
        return sourceList.stream()
                .filter(Objects::nonNull)
                .map(source -> convert(source, targetClass, ignoreProperties))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    /**
     * 将源对象集合转换为目标类型对象集合
     * 
     * @param sourceCollection 源对象集合
     * @param targetClass 目标类型
     * @param <S> 源类型泛型
     * @param <T> 目标类型泛型
     * @return 转换后的对象集合，源集合为null或空时返回空集合
     */
    public static <S, T> Set<T> convertSet(Collection<S> sourceCollection, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceCollection)) {
            return new HashSet<>();
        }
        
        return sourceCollection.stream()
                .filter(Objects::nonNull)
                .map(source -> convert(source, targetClass))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
    
    // ========== 自定义转换方法 ==========
    
    /**
     * 使用自定义转换器转换对象
     * 
     * @param source 源对象
     * @param converter 自定义转换器
     * @param <S> 源类型泛型
     * @param <T> 目标类型泛型
     * @return 转换后的对象，源对象为null时返回null
     */
    public static <S, T> T convert(S source, Function<S, T> converter) {
        if (source == null || converter == null) {
            return null;
        }
        
        try {
            return converter.apply(source);
        } catch (Exception e) {
            log.error("Custom conversion failed for source: {}", source.getClass().getSimpleName(), e);
            return null;
        }
    }
    
    /**
     * 使用自定义转换器转换对象列表
     * 
     * @param sourceList 源对象列表
     * @param converter 自定义转换器
     * @param <S> 源类型泛型
     * @param <T> 目标类型泛型
     * @return 转换后的对象列表，源列表为null或空时返回空列表
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Function<S, T> converter) {
        if (CollectionUtils.isEmpty(sourceList) || converter == null) {
            return new ArrayList<>();
        }
        
        return sourceList.stream()
                .filter(Objects::nonNull)
                .map(source -> convert(source, converter))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    // ========== 空值处理方法 ==========
    
    /**
     * 转换对象并处理空值（为空值字段设置默认值）
     * 
     * @param source 源对象
     * @param targetClass 目标类型
     * @param defaultValues 默认值映射（字段名 -> 默认值）
     * @param <T> 目标类型泛型
     * @return 转换后的对象，源对象为null时返回null
     */
    public static <T> T convertWithDefaults(Object source, Class<T> targetClass, Map<String, Object> defaultValues) {
        T target = convert(source, targetClass);
        if (target == null || CollUtil.isEmpty(defaultValues)) {
            return target;
        }
        
        try {
            // 为空值字段设置默认值
            defaultValues.forEach((fieldName, defaultValue) -> {
                Object fieldValue = BeanUtil.getFieldValue(target, fieldName);
                if (ObjectUtil.isEmpty(fieldValue)) {
                    BeanUtil.setFieldValue(target, fieldName, defaultValue);
                }
            });
            return target;
        } catch (Exception e) {
            log.error("Setting default values failed for target: {}", targetClass.getSimpleName(), e);
            return target;
        }
    }
    
    /**
     * 转换对象并移除空值字段
     * 
     * @param source 源对象
     * @param targetClass 目标类型
     * @param <T> 目标类型泛型
     * @return 转换后的对象，源对象为null时返回null
     */
    public static <T> T convertIgnoreNull(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtil.copyProperties(source, target, true);
            return target;
        } catch (Exception e) {
            log.error("Bean conversion ignoring null failed: {} -> {}", 
                     source.getClass().getSimpleName(), targetClass.getSimpleName(), e);
            return null;
        }
    }
    
    // ========== 性能优化方法 ==========
    
    /**
     * 批量转换（适用于大量数据转换，使用并行流提高性能）
     * 
     * @param sourceList 源对象列表
     * @param targetClass 目标类型
     * @param <S> 源类型泛型
     * @param <T> 目标类型泛型
     * @return 转换后的对象列表，源列表为null或空时返回空列表
     */
    public static <S, T> List<T> convertListParallel(List<S> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }
        
        // 当数据量较大时使用并行流
        if (sourceList.size() > 1000) {
            return sourceList.parallelStream()
                    .filter(Objects::nonNull)
                    .map(source -> convert(source, targetClass))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } else {
            return convertList(sourceList, targetClass);
        }
    }
    
    /**
     * 清空转换器缓存
     */
    public static void clearCache() {
        CONVERTER_CACHE.clear();
        log.info("Bean converter cache cleared");
    }
    
    /**
     * 获取缓存大小
     * 
     * @return 缓存大小
     */
    public static int getCacheSize() {
        return CONVERTER_CACHE.size();
    }
    
    // ========== 类型检查方法 ==========
    
    /**
     * 检查两个类是否可以进行转换
     * 
     * @param sourceClass 源类型
     * @param targetClass 目标类型
     * @return 是否可以转换
     */
    public static boolean canConvert(Class<?> sourceClass, Class<?> targetClass) {
        if (sourceClass == null || targetClass == null) {
            return false;
        }
        
        // 相同类型可以转换
        if (sourceClass.equals(targetClass)) {
            return true;
        }
        
        // 检查是否有公共字段
        try {
            Object sourceInstance = sourceClass.getDeclaredConstructor().newInstance();
            Object targetInstance = targetClass.getDeclaredConstructor().newInstance();
            BeanUtil.copyProperties(sourceInstance, targetInstance);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}