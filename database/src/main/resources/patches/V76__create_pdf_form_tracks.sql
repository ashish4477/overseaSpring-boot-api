CREATE TABLE `pdf_form_tracks` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`created` DATETIME NULL,
	`status` INT(6) NOT NULL DEFAULT '0',
	`file_name` TEXT NOT NULL,
	`hash` VARCHAR(50) NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

ALTER TABLE `pdf_form_tracks`
	ADD INDEX `hash_key` (`hash`);