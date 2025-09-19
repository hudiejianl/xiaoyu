package com.xiaoyua.validation.validator;

import cn.hutool.core.util.StrUtil;
import com.xiaoyua.validation.annotation.Mobile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 手机号验证器
 * 验证中国大陆手机号码格式
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {
    
    /**
     * 中国大陆手机号正则表达式
     * 支持13x, 14x, 15x, 16x, 17x, 18x, 19x号段
     */
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    
    private boolean allowEmpty;
    
    @Override
    public void initialize(Mobile constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 空值处理
        if (StrUtil.isEmpty(value)) {
            return allowEmpty;
        }
        
        // 验证手机号格式
        return MOBILE_PATTERN.matcher(value).matches();
    }
}