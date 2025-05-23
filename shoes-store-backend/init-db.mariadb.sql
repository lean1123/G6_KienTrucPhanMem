-- Tạo database nếu chưa tồn tại
CREATE DATABASE IF NOT EXISTS cart_service;


CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'password';

-- Gán quyền cho user đối với từng database
GRANT ALL PRIVILEGES ON cart_service.* TO 'user'@'%';


FLUSH PRIVILEGES;

