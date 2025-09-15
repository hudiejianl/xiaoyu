
-- 1. 用户主表
CREATE TABLE users (
                       id                   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户主键',
                       nickname             VARCHAR(30)  NOT NULL COMMENT '昵称',
                       avatar_url           VARCHAR(255) COMMENT '头像 OSS 地址',
                       birthday             DATE COMMENT '生日',
                       gender               TINYINT DEFAULT 0 COMMENT '性别：0未知 1男 2女',
                       campus_id            BIGINT COMMENT '所属校区 ID',
                       qq_openid            VARCHAR(64) UNIQUE COMMENT 'QQ 授权 openid',
                       mobile               CHAR(11) UNIQUE COMMENT '手机号',
                       real_name            VARCHAR(30) COMMENT '实名姓名',
                       id_card_no           CHAR(18) COMMENT '身份证号',
                       is_real_name         TINYINT DEFAULT 0 COMMENT '是否已实名：0否 1是',
                       privacy_mobile       TINYINT DEFAULT 0 COMMENT '手机号可见范围：0公开 1好友 2仅自己',
                       privacy_birthday     TINYINT DEFAULT 0 COMMENT '生日可见范围',
                       privacy_fans         TINYINT DEFAULT 0 COMMENT '粉丝列表可见范围',
                       status               TINYINT DEFAULT 0 COMMENT '账号状态：0正常 1封号',
                       created_at           DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                       updated_at           DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                       FULLTEXT(nickname) COMMENT '昵称全文索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户主表';

-- 2. 多方式登录授权
CREATE TABLE user_auths (
                            id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
                            user_id       BIGINT NOT NULL COMMENT '用户 ID',
                            identity_type ENUM('QQ','WECHAT','MOBILE','APPLE') COMMENT '授权方式',
                            identifier    VARCHAR(128) NOT NULL COMMENT '唯一标识：openid/手机号',
                            credential    VARCHAR(255) COMMENT '密码或 access_token',
                            verified_at   DATETIME COMMENT '验证通过时间',
                            UNIQUE KEY uk_type_id (identity_type, identifier)
#                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户授权表';

-- 3. 角色与权限
CREATE TABLE roles (
                       id   INT AUTO_INCREMENT PRIMARY KEY COMMENT '角色 ID',
                       name VARCHAR(30) UNIQUE COMMENT '角色名称：USER、ADMIN 等'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色字典表';

CREATE TABLE user_roles (
                            user_id BIGINT COMMENT '用户 ID',
                            role_id INT COMMENT '角色 ID',
                            PRIMARY KEY (user_id, role_id)
#                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
#                             FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 4. 黑名单
CREATE TABLE blacklists (
                            id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
                            owner_id   BIGINT NOT NULL COMMENT '拉黑者 UID',
                            target_id  BIGINT NOT NULL COMMENT '被拉黑者 UID',
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '拉黑时间',
                            UNIQUE KEY uk_ot (owner_id, target_id)
#                             FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
#                             FOREIGN KEY (target_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='黑名单';

-- 5. 文件资源
CREATE TABLE files (
                       id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文件主键',
                       user_id    BIGINT NOT NULL COMMENT '上传者 UID',
                       biz_type   ENUM('AVATAR','BG','POST','TASK','COMMENT') COMMENT '业务类型',
                       file_url   VARCHAR(255) NOT NULL COMMENT 'OSS 原图地址',
                       thumb_url  VARCHAR(255) COMMENT 'CDN 缩略图地址',
                       size       INT COMMENT '文件大小（字节）',
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间'
#                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件资源表';

-- 6. 标签体系
CREATE TABLE tags (
                      id       INT AUTO_INCREMENT PRIMARY KEY COMMENT '标签 ID',
                      name     VARCHAR(30) UNIQUE COMMENT '标签名称',
                      category ENUM('TASK','POST') COMMENT '适用业务',
                      weight   INT DEFAULT 0 COMMENT '后台排序权重',
                      is_hot   TINYINT DEFAULT 0 COMMENT '是否热门：0否 1是'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签字典表';

CREATE TABLE tag_items (
                           id        BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
                           tag_id    INT COMMENT '标签 ID',
                           item_id   BIGINT COMMENT '业务对象 ID',
                           item_type ENUM('POST','TASK') COMMENT '业务类型'
#                            FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签与业务对象关联表';

-- 7. 朋友圈（动态）
CREATE TABLE posts (
                       id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '动态 ID',
                       user_id     BIGINT NOT NULL COMMENT '发布者 UID',
                       content     TEXT COMMENT '文本内容',
                       visibility  ENUM('PUBLIC','FRIEND','CAMPUS') DEFAULT 'PUBLIC' COMMENT '可见范围',
                       poi_lat     DECIMAL(10,6) COMMENT '纬度',
                       poi_lng     DECIMAL(10,6) COMMENT '经度',
                       poi_name    VARCHAR(100) COMMENT 'POI 名称',
                       is_top      TINYINT DEFAULT 0 COMMENT '是否置顶：0否 1是',
                       status      ENUM('DRAFT','PUBLISHED','HIDDEN','AUDITING','REJECTED') DEFAULT 'PUBLISHED' COMMENT '状态',
                       created_at  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                       updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                       FULLTEXT(content) COMMENT '内容全文索引'
#                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='朋友圈动态表';

CREATE TABLE post_files (
                            post_id BIGINT COMMENT '动态 ID',
                            file_id BIGINT COMMENT '文件 ID',
                            sort    TINYINT DEFAULT 0 COMMENT '顺序号',
                            PRIMARY KEY (post_id, file_id)
#                             FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
#                             FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态-文件关联表';

-- 8. 点赞 / 收藏 / 转发 / 评论

-- =========== 点赞表 ===========
ALTER TABLE likes
DROP PRIMARY KEY,
    ADD COLUMN id BIGINT AUTO_INCREMENT FIRST,
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY uk_likes_uid_item (user_id, item_id, item_type);

-- =========== 收藏表 ===========
ALTER TABLE favorites
DROP PRIMARY KEY,
    ADD COLUMN id BIGINT AUTO_INCREMENT FIRST,
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY uk_fav_uid_item (user_id, item_id, item_type);

CREATE TABLE likes (
                       user_id    BIGINT COMMENT '点赞者 UID',
                       item_id    BIGINT COMMENT '业务对象 ID',
                       item_type  ENUM('POST','TASK','COMMENT') COMMENT '业务类型',
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
                       PRIMARY KEY (user_id, item_id, item_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

CREATE TABLE favorites (
                            user_id    BIGINT COMMENT '收藏者 UID',
                           item_id    BIGINT COMMENT '业务对象 ID',
                           item_type  ENUM('POST','TASK') COMMENT '业务类型',
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
                           PRIMARY KEY (user_id, item_id, item_type)
)    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

CREATE TABLE shares (
                        id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '转发 ID',
                        user_id    BIGINT COMMENT '转发者 UID',
                        item_id    BIGINT COMMENT '业务对象 ID',
                        item_type  ENUM('POST','TASK','COMMENT') COMMENT '业务类型',
                        reason     VARCHAR(255) COMMENT '转发附言',
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '转发时间'
#                         FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='转发记录表';

CREATE TABLE comments (
                          id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论 ID',
                          user_id    BIGINT COMMENT '评论者 UID',
                          item_id    BIGINT COMMENT '业务对象 ID',
                          item_type  ENUM('POST','TASK') COMMENT '业务类型',
                          parent_id  BIGINT DEFAULT 0 COMMENT '父评论 ID，0 为一级',
                          content    TEXT COMMENT '评论内容',
                          at_users   JSON COMMENT '@用户 JSON 数组',
                          status     ENUM('VISIBLE','HIDDEN','AUDITING','REJECTED') DEFAULT 'VISIBLE' COMMENT '可见状态',
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
#                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                          FULLTEXT(content) COMMENT '内容全文索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 9. 任务系统
CREATE TABLE tasks (
                       id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '任务 ID',
                       publisher_id  BIGINT NOT NULL COMMENT '发布者 UID',
                       title         VARCHAR(100) NOT NULL COMMENT '任务标题',
                       content       TEXT COMMENT '任务详情',
                       reward        DECIMAL(10,2) COMMENT '悬赏金额',
                       status        ENUM('DRAFT','AUDITING','RECRUIT','RUNNING','DELIVER','FINISH','CLOSED','ARBITRATED') DEFAULT 'DRAFT' COMMENT '生命周期状态',
                       visibility    ENUM('PUBLIC','FRIEND','CAMPUS') DEFAULT 'PUBLIC' COMMENT '可见范围',
                       expire_at     DATETIME COMMENT '截止报名时间',
                       created_at    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                       updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                       FULLTEXT(title, content) COMMENT '标题+详情全文索引'
#                        FOREIGN KEY (publisher_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务主表';

CREATE TABLE task_files (
                            task_id BIGINT COMMENT '任务 ID',
                            file_id BIGINT COMMENT '文件 ID',
                            PRIMARY KEY (task_id, file_id)
#                             FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
#                             FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务-文件关联表';

CREATE TABLE task_orders (
                             id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单主键',
                             task_id      BIGINT NOT NULL COMMENT '任务 ID',
                             receiver_id  BIGINT NOT NULL COMMENT '接单者 UID',
                             status       ENUM('WAIT_ACCEPT','ACCEPTED','REFUSED','CANCELLED','FINISH') DEFAULT 'WAIT_ACCEPT' COMMENT '订单状态',
                             created_at   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             UNIQUE KEY uk_task_recv (task_id, receiver_id)
#                              FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
#                              FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务订单表';

CREATE TABLE task_reviews (
                              id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评价 ID',
                              task_id     BIGINT COMMENT '任务 ID',
                              reviewer_id BIGINT COMMENT '评价者 UID',
                              reviewee_id BIGINT COMMENT '被评价者 UID',
                              role_type   ENUM('PUBLISHER','RECEIVER') COMMENT '评价方角色',
                              score       TINYINT CHECK (score BETWEEN 1 AND 5) COMMENT '1~5 星',
                              tags        JSON COMMENT '评价标签 JSON 数组',
                              content     VARCHAR(500) COMMENT '文字评价',
                              created_at  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间'
#                               FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务双向评价表';

-- 10. 消息中心
CREATE TABLE notifications (
                               id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '通知 ID',
                               user_id    BIGINT NOT NULL COMMENT '接收者 UID',
                               type       ENUM('LIKE','FAVORITE','COMMENT','SHARE','TASK_ORDER','SYSTEM','VIOLATION') COMMENT '通知类型',
                               title      VARCHAR(100) COMMENT '标题',
                               content    TEXT COMMENT '正文',
                               ref_id     BIGINT COMMENT '关联业务对象 ID',
                               ref_type   ENUM('POST','TASK','COMMENT') COMMENT '关联业务类型',
                               status     ENUM('UNREAD','READ') DEFAULT 'UNREAD' COMMENT '阅读状态',
                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '通知时间'
#                                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';

-- 11. 搜索相关
CREATE TABLE search_hotwords (
                                 id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '热词 ID',
                                 keyword    VARCHAR(100) UNIQUE COMMENT '关键词',
                                 search_cnt INT DEFAULT 0 COMMENT '搜索次数',
                                 weight     INT DEFAULT 0 COMMENT '后台手动权重',
                                 is_hot     TINYINT DEFAULT 0 COMMENT '是否上榜：0否 1是',
                                 created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='热搜词库表';

CREATE TABLE search_blacklist (
                                  keyword VARCHAR(100) PRIMARY KEY COMMENT '屏蔽关键词'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='搜索黑名单';

-- 12. 个人主页
CREATE TABLE homepages (
                           user_id    BIGINT PRIMARY KEY COMMENT '用户 ID',
                           visit_cnt  INT DEFAULT 0 COMMENT '被访问次数',
                           last_visit DATETIME COMMENT '最近一次访问时间'
#                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='个人主页统计表';

CREATE TABLE visit_logs (
                            id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志 ID',
                            user_id    BIGINT NOT NULL COMMENT '被访问者 UID',
                            visitor_id BIGINT NOT NULL COMMENT '访问者 UID',
                            visit_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间'
#                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
#                             FOREIGN KEY (visitor_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='主页访问日志表';

-- 13. 好友系统
CREATE TABLE friendships (
                             id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '好友关系 ID',
                             user_id    BIGINT COMMENT '用户 UID',
                             friend_id  BIGINT COMMENT '好友 UID',
                             status     ENUM('PENDING','ACCEPTED','REFUSED') DEFAULT 'PENDING' COMMENT '好友状态',
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             UNIQUE KEY uk_uf (user_id, friend_id)
#                              FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
#                              FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='好友关系表';

CREATE TABLE friend_messages (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '消息ID',
                      from_id BIGINT NOT NULL COMMENT '发送者ID',
                      to_id BIGINT NOT NULL COMMENT '接收者ID',
                      content TEXT NOT NULL COMMENT '消息内容',
                      message_type VARCHAR(20) DEFAULT 'TEXT' COMMENT '消息类型：TEXT/IMAGE/FILE',
                      status VARCHAR(20) DEFAULT 'SENT' COMMENT '消息状态：SENT/DELIVERED/READ',
                      is_read TINYINT DEFAULT 0 COMMENT '是否已读：0未读 1已读',
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                      deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记：0正常 1删除',
                      INDEX idx_from_to (from_id, to_id, created_at),
                      INDEX idx_to_unread (to_id, is_read, created_at),
                      INDEX idx_created_at (created_at)
#                      FOREIGN KEY (from_id) REFERENCES users(id) ON DELETE CASCADE,
#                      FOREIGN KEY (to_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='私信消息表';

-- 14. 校区字典
CREATE TABLE campuses (
                          id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '校区 ID',
                          name       VARCHAR(100) UNIQUE COMMENT '校区名称',
                          province   VARCHAR(30) COMMENT '省',
                          city       VARCHAR(30) COMMENT '市',
                          address    TEXT COMMENT '详细地址',
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='校区字典表';

-- 15. 运营后台
CREATE TABLE sensitive_words (
                                 word  VARCHAR(50) PRIMARY KEY COMMENT '敏感词',
                                 level TINYINT DEFAULT 1 COMMENT '危险等级：1低 2中 3高'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='敏感词库表';

CREATE TABLE system_notices (
                                id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '公告 ID',
                                title      VARCHAR(100) COMMENT '公告标题',
                                content    TEXT COMMENT '公告正文',
                                start_time DATETIME COMMENT '生效开始时间',
                                end_time   DATETIME COMMENT '生效结束时间',
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

-- 16. 数据统计
CREATE TABLE post_stats (
                            post_id     BIGINT PRIMARY KEY COMMENT '动态 ID',
                            view_cnt    INT DEFAULT 0 COMMENT '浏览量',
                            like_cnt    INT DEFAULT 0 COMMENT '点赞数',
                            comment_cnt INT DEFAULT 0 COMMENT '评论数',
                            share_cnt   INT DEFAULT 0 COMMENT '转发数',
                            created_at  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
#                             FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态统计表';

CREATE TABLE task_stats (
                            task_id    BIGINT PRIMARY KEY COMMENT '任务 ID',
                            view_cnt   INT DEFAULT 0 COMMENT '浏览量',
                            order_cnt  INT DEFAULT 0 COMMENT '接单数',
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
#                             FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务统计表';

-- 17. 举报投诉
CREATE TABLE reports (
                         id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '举报 ID',
                         user_id    BIGINT COMMENT '举报人 UID',
                         item_id    BIGINT COMMENT '被举报对象 ID',
                         item_type  ENUM('POST','TASK','COMMENT','USER') COMMENT '被举报类型',
                         reason     VARCHAR(255) COMMENT '举报理由',
                         status     ENUM('PENDING','ACCEPTED','REFUSED') DEFAULT 'PENDING' COMMENT '处理状态',
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间'
#                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='举报记录表';

-- 18. 话题（#tag#）
CREATE TABLE topics (
                        id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '话题 ID',
                        name        VARCHAR(50) UNIQUE COMMENT '话题名称（带 #）',
                        description TEXT COMMENT '话题描述',
                        post_cnt    INT DEFAULT 0 COMMENT '关联动态数',
                        created_at  DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='话题字典表';

CREATE TABLE topic_posts (
                             topic_id BIGINT COMMENT '话题 ID',
                             post_id  BIGINT COMMENT '动态 ID',
                             PRIMARY KEY (topic_id, post_id)
#                              FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE,
#                              FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='话题-动态关联表';
