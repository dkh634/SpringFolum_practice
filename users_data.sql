-- ==============================
-- 初期データ
-- ==============================
-- グループ
INSERT INTO groups (name) VALUES ('ADMIN');
INSERT INTO groups (name) VALUES ('USER');
INSERT INTO groups (name) VALUES ('MODERATOR');

-- ユーザー
INSERT INTO users (username, password, enabled, delete_flag) VALUES 
('admin', '$2a$12$Ivi6.YbOiwvphBLmj8k5N.JZoZ8ra78Zt6yTY3UA1zP3fOa0vqeiW', true, false),
('taro', '$2a$12$bpv3Jx/BQ2pqTgJTztqHMOswy1JLoUlrEE3WUtRnFZxQLStLAZvjm', true, false),
('hanako', '$2a$12$5kIjoI6VaEidUpbg0w9lz.IfO.u7oMHOoUSOmNExXW9Vw5LbPqQwu', true, false);

-- 中間テーブル（ユーザーとグループの紐付け）
-- admin は ADMIN グループ
INSERT INTO user_groups (user_id, group_id) VALUES (1, 1);

-- taro は USER グループ
INSERT INTO user_groups (user_id, group_id) VALUES (2, 2);

-- hanako は USER と MODERATOR グループ
INSERT INTO user_groups (user_id, group_id) VALUES (3, 2);
INSERT INTO user_groups (user_id, group_id) VALUES (3, 3);
