ALTER TABLE `email`
    ADD COLUMN `retry_time` DATETIME NULL DEFAULT NULL AFTER `updated_time`;

ALTER TABLE `email`
	  ADD INDEX `idx_retry_time` (`retry_time`);

ALTER TABLE `email`
    ADD COLUMN `priority` INT(11) NOT NULL DEFAULT 30 AFTER `attempt_number`;

