-- 创建离线消息表
CREATE TABLE offline_messages (
    id BIGINT PRIMARY KEY COMMENT '离线消息ID',
    user_id BIGINT NOT NULL COMMENT '接收者用户ID',
    message_type VARCHAR(20) NOT NULL COMMENT '消息类型：NOTIFICATION通知 PRIVATE_MESSAGE私信',
    original_message_id BIGINT NULL COMMENT '原始消息ID（如果是私信消息）',
    notification_id BIGINT NULL COMMENT '通知ID（如果是通知消息）',
    message_content TEXT NOT NULL COMMENT '消息内容JSON',
    from_user_id BIGINT NULL COMMENT '发送者用户ID',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '消息状态：PENDING待推送 PUSHED已推送 EXPIRED已过期',
    retry_count INT NOT NULL DEFAULT 0 COMMENT '重试次数',
    expire_at DATETIME NOT NULL COMMENT '过期时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0正常 1删除'
) COMMENT='离线消息表';

-- 创建索引
CREATE INDEX idx_offline_messages_user_id_status ON offline_messages(user_id, status);
CREATE INDEX idx_offline_messages_expire_at ON offline_messages(expire_at);
CREATE INDEX idx_offline_messages_created_at ON offline_messages(created_at);
CREATE INDEX idx_offline_messages_message_type ON offline_messages(message_type);
