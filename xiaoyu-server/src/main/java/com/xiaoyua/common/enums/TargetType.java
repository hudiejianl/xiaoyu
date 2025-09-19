package com.xiaoyua.common.enums;

/**
 * 业务目标类型枚举，替代各处的魔法字符串，如 "POST"、"COMMENT"、"TASK"。
 */
public enum TargetType {
    POST,
    COMMENT,
    TASK;

    public String value() {
        return this.name();
    }

    public static TargetType fromString(String s) {
        if (s == null) return null;
        return TargetType.valueOf(s.trim().toUpperCase());
    }
}
