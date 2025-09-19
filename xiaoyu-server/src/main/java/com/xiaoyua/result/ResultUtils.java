package com.xiaoyua.result;

import com.xiaoyua.vo.common.PageResult;

import java.util.List;

/**
 * 响应结果工具类
 * 提供常用的响应构建方法
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
public class ResultUtils {
    
    /**
     * 私有构造函数，防止实例化
     */
    private ResultUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    // ========== 数据响应方法 ==========
    
    /**
     * 返回列表数据
     * 
     * @param list 数据列表
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<List<T>> list(List<T> list) {
        return Result.success("查询成功", list);
    }
    
    /**
     * 返回分页数据
     * 
     * @param list 数据列表
     * @param page 当前页码
     * @param size 每页数量
     * @param total 总记录数
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<PageResult<T>> page(List<T> list, Integer page, Integer size, Long total) {
        PageResult<T> pageResult = PageResult.of(list, page, size, total);
        return Result.success(pageResult);
    }
    
    /**
     * 返回空分页数据
     * 
     * @param page 当前页码
     * @param size 每页数量
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<PageResult<T>> emptyPage(Integer page, Integer size) {
        PageResult<T> pageResult = PageResult.empty(page, size);
        return Result.success(pageResult);
    }
    
    /**
     * 返回单个对象数据
     * 
     * @param data 数据对象
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> data(T data) {
        return Result.success("查询成功", data);
    }
    
    // ========== 操作响应方法 ==========
    
    /**
     * 创建成功响应
     * 
     * @param data 创建的数据
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> created(T data) {
        return Result.success("创建成功", data);
    }
    
    /**
     * 创建成功响应（无数据）
     * 
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> created() {
        return Result.success("创建成功");
    }
    
    /**
     * 更新成功响应
     * 
     * @param data 更新的数据
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> updated(T data) {
        return Result.success("更新成功", data);
    }
    
    /**
     * 更新成功响应（无数据）
     * 
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> updated() {
        return Result.success("更新成功");
    }
    
    /**
     * 删除成功响应
     * 
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> deleted() {
        return Result.success("删除成功");
    }
    
    // ========== 验证响应方法 ==========
    
    /**
     * 参数验证失败响应
     * 
     * @param fieldName 字段名
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> validationError(String fieldName, String message) {
        return Result.badRequest(String.format("%s: %s", fieldName, message));
    }
    
    /**
     * 业务验证失败响应
     * 
     * @param message 错误消息
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> businessValidationError(String message) {
        return Result.businessError(message);
    }
    
    // ========== 条件响应方法 ==========
    
    /**
     * 根据条件返回成功或失败响应
     * 
     * @param condition 条件
     * @param successMsg 成功消息
     * @param errorMsg 失败消息
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> Result<T> condition(boolean condition, String successMsg, String errorMsg) {
        return condition ? Result.success(successMsg) : Result.error(errorMsg);
    }
    
    /**
     * 根据数据是否为空返回响应
     * 
     * @param data 数据
     * @param notFoundMsg 数据不存在时的消息
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> Result<T> ofNullable(T data, String notFoundMsg) {
        return data != null ? Result.success(data) : Result.notFound(notFoundMsg);
    }
    
    /**
     * 根据数据是否为空返回响应（默认消息）
     * 
     * @param data 数据
     * @param <T> 数据类型
     * @return 响应结果
     */
    public static <T> Result<T> ofNullable(T data) {
        return ofNullable(data, "数据不存在");
    }
}