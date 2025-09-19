package com.xiaoyua.dto.auth;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MobileLoginDTO 验证功能测试
 * 测试手机号登录DTO的验证注解和边界条件
 * 
 * @author xiaoyu
 */
@DisplayName("手机号登录DTO验证测试")
class MobileLoginDTOTest {
    
    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    @DisplayName("有效的手机号和验证码应该通过验证")
    void testValidMobileAndCode() {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO("13812345678", "123456");
        
        // When
        Set<ConstraintViolation<MobileLoginDTO>> violations = validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty(), "有效数据应该通过验证");
    }
    
    @Test
    @DisplayName("手机号为空时应该验证失败")
    void testBlankMobile() {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO("", "123456");
        
        // When
        Set<ConstraintViolation<MobileLoginDTO>> violations = validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<MobileLoginDTO> violation = violations.iterator().next();
        assertEquals("手机号不能为空", violation.getMessage());
        assertEquals("mobile", violation.getPropertyPath().toString());
    }
    
    @Test
    @DisplayName("手机号为null时应该验证失败")
    void testNullMobile() {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO(null, "123456");
        
        // When
        Set<ConstraintViolation<MobileLoginDTO>> violations = validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<MobileLoginDTO> violation = violations.iterator().next();
        assertEquals("手机号不能为空", violation.getMessage());
    }
    
    @ParameterizedTest
    @DisplayName("无效的手机号格式应该验证失败")
    @ValueSource(strings = {
        "1234567890",    // 不是1开头
        "12345678901",   // 11位但不是1开头
        "1081234567",    // 第二位是0
        "1121234567",    // 第二位是1
        "1221234567",    // 第二位是2
        "138123456789",  // 12位
        "1381234567",    // 10位
        "138abcd5678",   // 包含字母
        "138-1234-5678", // 包含特殊字符
        " 13812345678",  // 前面有空格
        "13812345678 "   // 后面有空格
    })
    void testInvalidMobileFormat(String invalidMobile) {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO(invalidMobile, "123456");
        
        // When
        Set<ConstraintViolation<MobileLoginDTO>> violations = validator.validate(dto);
        
        // Then
        assertFalse(violations.isEmpty(), "无效手机号格式应该验证失败: " + invalidMobile);
        boolean hasPatternViolation = violations.stream()
            .anyMatch(v -> "手机号格式不正确".equals(v.getMessage()));
        assertTrue(hasPatternViolation, "应该包含手机号格式错误信息");
    }
    
    @ParameterizedTest
    @DisplayName("有效的手机号格式应该通过验证")
    @ValueSource(strings = {
        "13812345678",   // 138开头
        "15912345678",   // 159开头
        "18612345678",   // 186开头
        "17712345678",   // 177开头
        "19912345678"    // 199开头
    })
    void testValidMobileFormat(String validMobile) {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO(validMobile, "123456");
        
        // When
        Set<ConstraintViolation<MobileLoginDTO>> violations = validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty(), "有效手机号格式应该通过验证: " + validMobile);
    }
    
    @Test
    @DisplayName("验证码为空时应该验证失败")
    void testBlankCode() {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO("13812345678", "");
        
        // When
        Set<ConstraintViolation<MobileLoginDTO>> violations = validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<MobileLoginDTO> violation = violations.iterator().next();
        assertEquals("验证码不能为空", violation.getMessage());
        assertEquals("code", violation.getPropertyPath().toString());
    }
    
    @Test
    @DisplayName("验证码为null时应该验证失败")
    void testNullCode() {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO("13812345678", null);
        
        // When
        Set<ConstraintViolation<MobileLoginDTO>> violations = validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<MobileLoginDTO> violation = violations.iterator().next();
        assertEquals("验证码不能为空", violation.getMessage());
    }
    
    @ParameterizedTest
    @DisplayName("无效的验证码格式应该验证失败")
    @ValueSource(strings = {
        "12345",      // 5位
        "1234567",    // 7位
        "12345a",     // 包含字母
        "12345 ",     // 包含空格
        " 123456",    // 前面有空格
        "123456 ",    // 后面有空格
        "12-34-56",   // 包含特殊字符
        "abcdef"      // 全字母
    })
    void testInvalidCodeFormat(String invalidCode) {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO("13812345678", invalidCode);
        
        // When
        Set<ConstraintViolation<MobileLoginDTO>> violations = validator.validate(dto);
        
        // Then
        assertFalse(violations.isEmpty(), "无效验证码格式应该验证失败: " + invalidCode);
        boolean hasPatternViolation = violations.stream()
            .anyMatch(v -> "验证码格式不正确".equals(v.getMessage()));
        assertTrue(hasPatternViolation, "应该包含验证码格式错误信息");
    }
    
    @ParameterizedTest
    @DisplayName("有效的验证码格式应该通过验证")
    @ValueSource(strings = {
        "123456",
        "000000",
        "999999",
        "654321"
    })
    void testValidCodeFormat(String validCode) {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO("13812345678", validCode);
        
        // When
        Set<ConstraintViolation<MobileLoginDTO>> violations = validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty(), "有效验证码格式应该通过验证: " + validCode);
    }
    
    @Test
    @DisplayName("手机号和验证码都无效时应该有两个验证错误")
    void testBothInvalid() {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO("invalid", "invalid");
        
        // When
        Set<ConstraintViolation<MobileLoginDTO>> violations = validator.validate(dto);
        
        // Then
        assertEquals(2, violations.size());
        
        boolean hasMobileError = violations.stream()
            .anyMatch(v -> "手机号格式不正确".equals(v.getMessage()));
        boolean hasCodeError = violations.stream()
            .anyMatch(v -> "验证码格式不正确".equals(v.getMessage()));
        
        assertTrue(hasMobileError, "应该包含手机号格式错误");
        assertTrue(hasCodeError, "应该包含验证码格式错误");
    }
    
    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO("13812345678", "123456");
        
        // When
        String result = dto.toString();
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("mobile"));
        assertTrue(result.contains("code"));
        assertTrue(result.contains("13812345678"));
        assertTrue(result.contains("123456"));
    }
    
    @Test
    @DisplayName("测试getter和setter方法")
    void testGettersAndSetters() {
        // Given
        MobileLoginDTO dto = new MobileLoginDTO();
        
        // When
        dto.setMobile("13812345678");
        dto.setCode("123456");
        
        // Then
        assertEquals("13812345678", dto.getMobile());
        assertEquals("123456", dto.getCode());
    }
    
    @Test
    @DisplayName("测试构造函数")
    void testConstructors() {
        // Test default constructor
        MobileLoginDTO dto1 = new MobileLoginDTO();
        assertNull(dto1.getMobile());
        assertNull(dto1.getCode());
        
        // Test parameterized constructor
        MobileLoginDTO dto2 = new MobileLoginDTO("13812345678", "123456");
        assertEquals("13812345678", dto2.getMobile());
        assertEquals("123456", dto2.getCode());
    }
}