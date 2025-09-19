package com.xiaoyua.validation.annotation;

import com.xiaoyua.validation.validator.MobileValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 手机号验证注解
 * 验证字符串是否为有效的中国大陆手机号码
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MobileValidator.class)
public @interface Mobile {
    
    /**
     * 错误消息
     */
    String message() default "{validation.mobile.invalid}";
    
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