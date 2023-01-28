CREATE TABLE `migration_ids` (
	`id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`object_id` INT(11) UNSIGNED NOT NULL DEFAULT '0',
	`migration_id` INT(11) UNSIGNED NOT NULL DEFAULT '0',
	`version` INT(6) NOT NULL DEFAULT '0',
	`class_name` VARCHAR(255) NULL DEFAULT '',
	PRIMARY KEY (`id`),
	INDEX `migration_key` (`migration_id`),
	INDEX `version_key` (`version`)

) ENGINE=InnoDB;
