ALTER TABLE `mailing_list_link`
	ADD COLUMN `error_count` INT(10) NOT NULL DEFAULT '0' AFTER `last_updated`;
