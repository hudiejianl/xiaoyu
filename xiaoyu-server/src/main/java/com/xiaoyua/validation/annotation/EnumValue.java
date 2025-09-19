package com.xiaoyua.validation.annotation;

import com.xiaoyua.validation.validator.EnumValueValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 枚举值验证注解
 * 验证字符串或数字是否在指定的枚举值范围内
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {
    
    /**
     * 错误消息
     */
    String message() default "{validation.enum.invalid}";
    
    /**
     * 验证分组
     */
    Class<?>[] groups() default {};
    
    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};
    
    /**
     * 允许的字符串值
     */
    String[] strValues() default {};
    
    /**
     * 允许的整数值
     */
    int[] intValues() default {};
    
    /**
     * 是否允许为空
     */
    boolean allowEmpty() default false;
}