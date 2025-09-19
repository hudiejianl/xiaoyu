package com.xiaoyua.validation.annotation;

import com.xiaoyua.validation.validator.IdCardValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 身份证号验证注解
 * 验证字符串是否为有效的中国大陆身份证号码
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IdCardValidator.class)
public @interface IdCard {
    
    /**
     * 错误消息
     */
    String message() default "{validation.idcard.invalid}";
    
    /**
     * 验证分组
     */
    Class<?>[] groups() default {};
    
    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 是否允许为空
     */
    boolean allowEmpty() default false;
}