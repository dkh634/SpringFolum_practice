-- ==============================
-- 初期データ
-- ==============================
-- グループ
INSERT INTO groups (name) VALUES ('ADMIN');
INSERT INTO groups (name) VALUES ('USER');
INSERT INTO groups (name) VALUES ('MODERATOR');

-- ユーザー
INSERT INTO users (username, password, enabled, delete_flag) VALUES 
('admin', 'admin123', true, false),
('taro', 'taro123', true, false),
('hanako', 'hanako123', true, false);

-- 中間テーブル（ユーザーとグループの紐付け）
-- admin は ADMIN グループ
INSERT INTO user_groups (user_id, group_id) VALUES (1, 1);

-- taro は USER グループ
INSERT INTO user_groups (user_id, group_id) VALUES (2, 2);

-- hanako は USER と MODERATOR グループ
INSERT INTO user_groups (user_id, group_id) VALUES (3, 2);
INSERT INTO user_groups (user_id, group_id) VALUES (3, 3);
