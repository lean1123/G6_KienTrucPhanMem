CREATE DATABASE IF NOT EXISTS order_service;
CREATE DATABASE IF NOT EXISTS payment_service;

CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON order_service.* TO 'user'@'%';
GRANT ALL PRIVILEGES ON payment_service.* TO 'user'@'%';

FLUSH PRIVILEGES;
