
-- 1. 为 users 表添加 username 字段
ALTER TABLE users ADD COLUMN username VARCHAR(50) UNIQUE COMMENT '用户名（用于登录）' AFTER id;

-- 2. 为 user_auths 表添加 PASSWORD 枚举值和时间戳字段
ALTER TABLE user_auths MODIFY COLUMN identity_type ENUM('QQ','WECHAT','MOBILE','APPLE','PASSWORD') COMMENT '授权方式';
ALTER TABLE user_auths ADD COLUMN created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER verified_at;
ALTER TABLE user_auths ADD COLUMN updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER created_at;

-- 3. 更新 user_auths 表注释
ALTER TABLE user_auths MODIFY COLUMN identifier VARCHAR(128) NOT NULL COMMENT '唯一标识：openid/手机号/用户名';


-- 5. 为测试用户创建密码认证
-- INSERT INTO user_auths (user_id, identity_type, identifier, credential, verified_at) 
-- VALUES (LAST_INSERT_ID(), 'PASSWORD', 'testuser', 'encoded_password_here', NOW());
-- 6. 为 topic_posts 表增加自增主键 id，并将 (topic_id, post_id) 调整为唯一键
-- 执行时间：2025-09-17
-- 说明：MyBatis-Plus 需要主键以支持 xxxById 等方法；原逻辑复合主键语义保留为唯一约束
ALTER TABLE topic_posts
  DROP PRIMARY KEY;

ALTER TABLE topic_posts
  ADD COLUMN id BIGINT AUTO_INCREMENT FIRST,
  ADD PRIMARY KEY (id);

ALTER TABLE topic_posts
  ADD UNIQUE KEY uk_topic_post (topic_id, post_id);

-- 7. 为 post_stats 表添加收藏计数字段 fav_cnt（如果不存在）
-- 执行时间：2025-09-18
ALTER TABLE post_stats
  ADD COLUMN IF NOT EXISTS fav_cnt INT DEFAULT 0 COMMENT '收藏数' AFTER like_cnt;


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
