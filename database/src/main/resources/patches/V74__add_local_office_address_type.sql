ALTER TABLE `local_officials`
	ADD COLUMN `local_office_type` VARCHAR(32) NOT NULL DEFAULT 'ALL' AFTER `updated`;