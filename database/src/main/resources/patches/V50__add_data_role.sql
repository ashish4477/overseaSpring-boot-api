ALTER TABLE `question_fields`
	ADD COLUMN `data_role` VARCHAR(255) NULL DEFAULT '' AFTER `additional_help`;
