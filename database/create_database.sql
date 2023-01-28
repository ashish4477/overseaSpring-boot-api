SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS overseas_foundation;
CREATE DATABASE overseas_foundation;
CREATE USER 'overseas' IDENTIFIED BY 'v0t!n9_4ud' DEFAULT CHARACTER SET utf8;
GRANT ALL PRIVILEGES ON overseas_foundation.* TO 'overseas'@'localhost';

SET FOREIGN_KEY_CHECKS = 1;