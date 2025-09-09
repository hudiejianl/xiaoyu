package com.xiaoyu.result;


import lombok.Data;

@Data
public class Result<T> {
    String msg;
    Integer code;
    T data;

    Result(){}

    Result(String msg, Integer code, T data){
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public static <T> Result<T> success(T data){
        return new Result<>("success", 200, data);
    }

    public static <T> Result<T> success(){
        return new Result<>("success", 200, null);
    }

    public static <T> Result<T> success(String msg){
        return new Result<>(msg, 200, null);
    }

    public static <T> Result<T> error(String msg){
        return new Result<>(msg, 500, null);
    }






}
