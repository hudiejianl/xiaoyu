package com.xiaoyua.validation.validator;

import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaoyua.validation.annotation.IdCard;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 身份证号验证器
 * 验证中国大陆身份证号码格式和校验码
 * 
 * @author xiaoyu
 * @since 1.0.0
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {
    
    private boolean allowEmpty;
    
    @Override
    public void initialize(IdCard constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 空值处理
        if (StrUtil.isEmpty(value)) {
            return allowEmpty;
        }
        
        // 使用 Hutool 工具验证身份证号
        return IdcardUtil.isValidCard(value);
    }
}