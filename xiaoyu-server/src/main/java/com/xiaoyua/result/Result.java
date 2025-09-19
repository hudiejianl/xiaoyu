package com.xiaoyua.result;

import com.xiaoyua.vo.common.PageResult;
import lombok.Data;

/**
 * 统一响应结果类
 * 用于包装所有接口的响应数据
 * 
 * @param <T> 数据类型
 * @author xiaoyu
 * @since 1.0.0
 */
@Data
public class Result<T> {
    
    /**
     * 响应消息
     */
    private String msg;
    
    /**
     * 响应状态码
     */
    private Integer code;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 请求时间戳
     */
    private Long timestamp;
    
    /**
     * 默认构造函数
     */
    Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 构造函数
     * 
     * @param msg 响应消息
     * @param code 响应状态码
     * @param data 响应数据
     */
    Result(String msg, Integer code, T data) {
        this.msg = msg;
        this.code = code;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    // ========== 成功响应方法 ==========
    
    /**
     * 成功响应（带数据）
     * 
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>("操作成功", 200, data);
    }
    
    /**
     * 成功响应（无数据）
     * 
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success() {
        return new Result<>("操作成功", 200, null);
    }
    
    /**
     * 成功响应（自定义消息）
     * 
     * @param msg 响应消息
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success(String msg) {
        return new Result<>(msg, 200, null);
    }
    
    /**
     * 成功响应（自定义消息和数据）
     * 
     * @param msg 响应消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(msg, 200, data);
    }
    
    /**
     * 分页数据成功响应
     * 
     * @param pageResult 分页结果
     * @param <T> 数据类型
     * @return 成功响应结果
     */
    public static <T> Result<PageResult<T>> success(PageResult<T> pageResult) {
        return new Result<>("查询成功", 200, pageResult);
    }
    
    // ========== 错误响应方法 ==========
    
    /**
     * 错误响应（默认500状态码）
     * 
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> error(String msg) {
        return new Result<>(msg, 500, null);
    }
    
    /**
     * 错误响应（自定义状态码）
     * 
     * @param code 状态码
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(msg, code, null);
    }
    
    /**
     * 错误响应（自定义状态码和数据）
     * 
     * @param code 状态码
     * @param msg 错误消息
     * @param data 响应数据
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> error(Integer code, String msg, T data) {
        return new Result<>(msg, code, data);
    }
    
    // ========== 常用错误响应方法 ==========
    
    /**
     * 参数错误响应（400）
     * 
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> badRequest(String msg) {
        return new Result<>(msg, 400, null);
    }
    
    /**
     * 未授权响应（401）
     * 
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> unauthorized(String msg) {
        return new Result<>(msg, 401, null);
    }
    
    /**
     * 禁止访问响应（403）
     * 
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> forbidden(String msg) {
        return new Result<>(msg, 403, null);
    }
    
    /**
     * 资源不存在响应（404）
     * 
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> notFound(String msg) {
        return new Result<>(msg, 404, null);
    }
    
    /**
     * 业务逻辑错误响应（422）
     * 
     * @param msg 错误消息
     * @param <T> 数据类型
     * @return 错误响应结果
     */
    public static <T> Result<T> businessError(String msg) {
        return new Result<>(msg, 422, null);
    }
    
    // ========== 判断方法 ==========
    
    /**
     * 判断是否成功
     * 
     * @return 是否成功
     */
    public boolean isSuccess() {
        return this.code != null && this.code == 200;
    }
    
    /**
     * 判断是否失败
     * 
     * @return 是否失败
     */
    public boolean isError() {
        return !isSuccess();
    }
}
