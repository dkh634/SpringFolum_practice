-- =================================
-- roles 初期データ
-- =================================
INSERT INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_USER');

-- =================================
-- users 初期データ
-- =================================
-- パスワードは BCrypt でハッシュ化して入れる必要があります
-- 例: password = "admin" をハッシュ化したもの
INSERT INTO users (username, password, enabled) VALUES
('admin', '$2a$10$7Q8xX9Pj2l6l3a2d0F0p5uC9hLk6yUO1RtQ/nvGJv1XHl9xH5lT3e', true),
('user',  '$2a$10$8Q7xY0Pj3l6l3b3e1F1q6uD9hJl6yUO1RtQ/nvGJv1XHl9xH5lT4f', true);

-- =================================
-- user_roles 初期データ
-- =================================
-- admin ユーザーに ROLE_ADMIN
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1),  -- admin -> ROLE_ADMIN
(2, 2);  -- user -> ROLE_USER
