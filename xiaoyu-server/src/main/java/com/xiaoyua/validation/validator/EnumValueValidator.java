package com.xiaoyua.validation.validator;

import cn.hutool.core.util.StrUtil;
import com.xiaoyua.validation.annotation.EnumValue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 枚举值验证器
 * 验证值是否在指定的枚举范围内
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {
    
    private Set<String> strValues;
    private Set<Integer> intValues;
    private boolean allowEmpty;
    
    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
        
        // 初始化字符串值集合
        if (constraintAnnotation.strValues().length > 0) {
            this.strValues = new HashSet<>(Arrays.asList(constraintAnnotation.strValues()));
        }
        
        // 初始化整数值集合
        if (constraintAnnotation.intValues().length > 0) {
            this.intValues = new HashSet<>();
            for (int value : constraintAnnotation.intValues()) {
                this.intValues.add(value);
            }
        }
    }
    
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // 空值处理
        if (value == null) {
            return allowEmpty;
        }
        
        // 字符串类型验证
        if (value instanceof String) {
            String strValue = (String) value;
            if (StrUtil.isEmpty(strValue)) {
                return allowEmpty;
            }
            return strValues != null && strValues.contains(strValue);
        }
        
        // 整数类型验证
        if (value instanceof Integer) {
            return intValues != null && intValues.contains((Integer) value);
        }
        
        // 其他数字类型转换为整数验证
        if (value instanceof Number) {
            int intValue = ((Number) value).intValue();
            return intValues != null && intValues.contains(intValue);
        }
        
        return false;
    }
}