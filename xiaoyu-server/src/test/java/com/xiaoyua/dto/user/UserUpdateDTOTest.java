package com.xiaoyua.dto.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UserUpdateDTO 验证功能测试
 * 测试用户更新DTO的验证注解和边界条件
 * 
 * @author xiaoyu
 */
@DisplayName("用户更新DTO验证测试")
class UserUpdateDTOTest {
    
    private Validator validator;
    
    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    @DisplayName("有效的用户更新数据应该通过验证")
    void testValidUserUpdate() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setNickname("测试用户");
        dto.setAvatarUrl("https://example.com/avatar.jpg");
        dto.setBirthday(LocalDate.of(2000, 1, 1));
        dto.setGender(1);
        dto.setCampusId(1L);
        dto.setPrivacyMobile(0);
        dto.setPrivacyBirthday(1);
        dto.setPrivacyFans(2);
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty(), "有效数据应该通过验证");
    }
    
    @Test
    @DisplayName("所有字段为null应该通过验证（可选字段）")
    void testAllNullFields() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty(), "所有可选字段为null应该通过验证");
    }
    
    @Test
    @DisplayName("昵称长度超过30个字符应该验证失败")
    void testNicknameTooLong() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setNickname("这是一个非常非常非常非常非常非常非常非常非常非常长的昵称超过三十个字符");
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserUpdateDTO> violation = violations.iterator().next();
        assertEquals("昵称最多30个字符", violation.getMessage());
        assertEquals("nickname", violation.getPropertyPath().toString());
    }
    
    @Test
    @DisplayName("昵称长度等于30个字符应该通过验证")
    void testNicknameExactly30Characters() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setNickname("这是一个刚好三十个字符的昵称测试用例数据内容");
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty(), "30个字符的昵称应该通过验证");
    }
    
    @Test
    @DisplayName("生日是未来日期应该验证失败")
    void testFutureBirthday() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setBirthday(LocalDate.now().plusDays(1));
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserUpdateDTO> violation = violations.iterator().next();
        assertEquals("生日必须是过去的日期", violation.getMessage());
        assertEquals("birthday", violation.getPropertyPath().toString());
    }
    
    @Test
    @DisplayName("生日是今天应该验证失败")
    void testTodayBirthday() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setBirthday(LocalDate.now());
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertEquals(1, violations.size());
        ConstraintViolation<UserUpdateDTO> violation = violations.iterator().next();
        assertEquals("生日必须是过去的日期", violation.getMessage());
    }
    
    @Test
    @DisplayName("生日是昨天应该通过验证")
    void testYesterdayBirthday() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setBirthday(LocalDate.now().minusDays(1));
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty(), "昨天的生日应该通过验证");
    }
    
    @ParameterizedTest
    @DisplayName("无效的性别值应该验证失败")
    @ValueSource(ints = {-1, 3, 4, 100})
    void testInvalidGender(int invalidGender) {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setGender(invalidGender);
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertFalse(violations.isEmpty(), "无效性别值应该验证失败: " + invalidGender);
        boolean hasGenderError = violations.stream()
            .anyMatch(v -> "性别值不正确".equals(v.getMessage()));
        assertTrue(hasGenderError, "应该包含性别值错误信息");
    }
    
    @ParameterizedTest
    @DisplayName("有效的性别值应该通过验证")
    @ValueSource(ints = {0, 1, 2})
    void testValidGender(int validGender) {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setGender(validGender);
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty(), "有效性别值应该通过验证: " + validGender);
    }
    
    @ParameterizedTest
    @DisplayName("无效的隐私设置值应该验证失败")
    @ValueSource(ints = {-1, 3, 4, 100})
    void testInvalidPrivacySettings(int invalidPrivacy) {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setPrivacyMobile(invalidPrivacy);
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertFalse(violations.isEmpty(), "无效隐私设置值应该验证失败: " + invalidPrivacy);
        boolean hasPrivacyError = violations.stream()
            .anyMatch(v -> "隐私设置值不正确".equals(v.getMessage()));
        assertTrue(hasPrivacyError, "应该包含隐私设置值错误信息");
    }
    
    @ParameterizedTest
    @DisplayName("有效的隐私设置值应该通过验证")
    @ValueSource(ints = {0, 1, 2})
    void testValidPrivacySettings(int validPrivacy) {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setPrivacyMobile(validPrivacy);
        dto.setPrivacyBirthday(validPrivacy);
        dto.setPrivacyFans(validPrivacy);
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertTrue(violations.isEmpty(), "有效隐私设置值应该通过验证: " + validPrivacy);
    }
    
    @Test
    @DisplayName("多个字段同时无效应该有多个验证错误")
    void testMultipleInvalidFields() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setNickname("这是一个非常非常非常非常非常非常非常非常非常非常长的昵称超过三十个字符");
        dto.setBirthday(LocalDate.now().plusDays(1));
        dto.setGender(-1);
        dto.setPrivacyMobile(3);
        dto.setPrivacyBirthday(4);
        dto.setPrivacyFans(5);
        
        // When
        Set<ConstraintViolation<UserUpdateDTO>> violations = validator.validate(dto);
        
        // Then
        assertEquals(6, violations.size(), "应该有6个验证错误");
        
        // 验证每个错误消息都存在
        Set<String> messages = violations.stream()
            .map(ConstraintViolation::getMessage)
            .collect(java.util.stream.Collectors.toSet());
        
        assertTrue(messages.contains("昵称最多30个字符"));
        assertTrue(messages.contains("生日必须是过去的日期"));
        assertTrue(messages.contains("性别值不正确"));
        assertTrue(messages.contains("隐私设置值不正确"));
    }
    
    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setNickname("测试用户");
        dto.setGender(1);
        
        // When
        String result = dto.toString();
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("nickname"));
        assertTrue(result.contains("gender"));
        assertTrue(result.contains("测试用户"));
        assertTrue(result.contains("1"));
    }
    
    @Test
    @DisplayName("测试getter和setter方法")
    void testGettersAndSetters() {
        // Given
        UserUpdateDTO dto = new UserUpdateDTO();
        LocalDate birthday = LocalDate.of(2000, 1, 1);
        
        // When
        dto.setNickname("测试用户");
        dto.setAvatarUrl("https://example.com/avatar.jpg");
        dto.setBirthday(birthday);
        dto.setGender(1);
        dto.setCampusId(1L);
        dto.setPrivacyMobile(0);
        dto.setPrivacyBirthday(1);
        dto.setPrivacyFans(2);
        
        // Then
        assertEquals("测试用户", dto.getNickname());
        assertEquals("https://example.com/avatar.jpg", dto.getAvatarUrl());
        assertEquals(birthday, dto.getBirthday());
        assertEquals(1, dto.getGender());
        assertEquals(1L, dto.getCampusId());
        assertEquals(0, dto.getPrivacyMobile());
        assertEquals(1, dto.getPrivacyBirthday());
        assertEquals(2, dto.getPrivacyFans());
    }
    
    @Test
    @DisplayName("测试构造函数")
    void testConstructors() {
        // Test default constructor
        UserUpdateDTO dto1 = new UserUpdateDTO();
        assertNull(dto1.getNickname());
        assertNull(dto1.getGender());
        
        // Test parameterized constructor
        LocalDate birthday = LocalDate.of(2000, 1, 1);
        UserUpdateDTO dto2 = new UserUpdateDTO("测试用户", "https://example.com/avatar.jpg", 
            birthday, 1, 1L, 0, 1, 2);
        
        assertEquals("测试用户", dto2.getNickname());
        assertEquals("https://example.com/avatar.jpg", dto2.getAvatarUrl());
        assertEquals(birthday, dto2.getBirthday());
        assertEquals(1, dto2.getGender());
        assertEquals(1L, dto2.getCampusId());
        assertEquals(0, dto2.getPrivacyMobile());
        assertEquals(1, dto2.getPrivacyBirthday());
        assertEquals(2, dto2.getPrivacyFans());
    }
}