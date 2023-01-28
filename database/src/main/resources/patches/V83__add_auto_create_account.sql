ALTER TABLE `faces_config`
    ADD COLUMN `auto_create_account` TINYINT(4) NOT NULL DEFAULT 0 AFTER `login_allowed`;