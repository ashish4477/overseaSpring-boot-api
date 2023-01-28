UPDATE `user_names` SET `title` = '' WHERE `title` IS NULL;
UPDATE `user_names` SET `first_name` = '' WHERE `first_name` IS NULL;
UPDATE `user_names` SET `last_name` = '' WHERE `last_name` IS NULL;
UPDATE `user_names` SET `middle_name` = '' WHERE `middle_name` IS NULL;
UPDATE `user_names` SET `suffix` = '' WHERE `suffix` IS NULL;

ALTER TABLE `user_names`
	CHANGE COLUMN `title` `title` VARCHAR(32) NOT NULL DEFAULT '' AFTER `id`,
	CHANGE COLUMN `first_name` `first_name` VARCHAR(128) NOT NULL DEFAULT '' AFTER `title`,
	CHANGE COLUMN `last_name` `last_name` VARCHAR(255) NOT NULL DEFAULT '' AFTER `first_name`,
	CHANGE COLUMN `middle_name` `middle_name` VARCHAR(255) NOT NULL DEFAULT '' AFTER `last_name`,
	CHANGE COLUMN `suffix` `suffix` VARCHAR(32) NOT NULL DEFAULT '' AFTER `middle_name`;

UPDATE `wizard_result_names` SET `title` = '' WHERE `title` IS NULL;
UPDATE `wizard_result_names` SET `first_name` = '' WHERE `first_name` IS NULL;
UPDATE `wizard_result_names` SET `last_name` = '' WHERE `last_name` IS NULL;
UPDATE `wizard_result_names` SET `middle_name` = '' WHERE `middle_name` IS NULL;
UPDATE `wizard_result_names` SET `suffix` = '' WHERE `suffix` IS NULL;

ALTER TABLE `wizard_result_names`
	CHANGE COLUMN `title` `title` VARCHAR(32) NOT NULL DEFAULT '' AFTER `id`,
	CHANGE COLUMN `first_name` `first_name` VARCHAR(128) NOT NULL DEFAULT '' AFTER `title`,
	CHANGE COLUMN `last_name` `last_name` VARCHAR(255) NOT NULL DEFAULT '' AFTER `first_name`,
	CHANGE COLUMN `middle_name` `middle_name` VARCHAR(255) NOT NULL DEFAULT '' AFTER `last_name`,
	CHANGE COLUMN `suffix` `suffix` VARCHAR(32) NOT NULL DEFAULT '' AFTER `middle_name`;
