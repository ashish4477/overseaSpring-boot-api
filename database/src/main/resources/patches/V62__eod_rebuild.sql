DROP TABLE IF EXISTS `local_officials_to_officers`;
DROP TABLE IF EXISTS `officers`;
CREATE TABLE `officers` (
	`id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
	`order_number` INT(10) UNSIGNED NOT NULL DEFAULT '0',
	`office_name` VARCHAR(255) NOT NULL DEFAULT '',
	`title` VARCHAR(255) NOT NULL DEFAULT '',
	`first_name` VARCHAR(255) NOT NULL DEFAULT '',
	`initial` VARCHAR(255) NOT NULL DEFAULT '',
	`last_name` VARCHAR(255) NOT NULL DEFAULT '',
	`suffix` VARCHAR(255) NOT NULL DEFAULT '',
	`phone` VARCHAR(255) NOT NULL DEFAULT '',
	`fax` VARCHAR(255) NOT NULL DEFAULT '',
	`email` VARCHAR(255) NOT NULL DEFAULT '',
	`region_id` INT(11) NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

INSERT INTO `officers` ( `order_number`, `office_name`, `title`, `first_name`,
	`initial`, `last_name`, `suffix`, `phone`,`fax`,`email`, `region_id`)
	SELECT 1, 'Local Election Official', lo.leo_title, lo.leo_first_name,
	lo.leo_initial, lo.leo_last_name, lo.leo_suffix, lo.leo_phone, lo.leo_fax, lo.leo_email, lo.id
	 FROM  `local_officials` lo WHERE lo.leo_first_name <> '' OR lo.leo_last_name <> ''
	 OR lo.leo_phone <> '' OR lo.leo_fax <> '' OR lo.leo_email <> '';

INSERT INTO `officers` ( `order_number`, `office_name`, `title`, `first_name`,
	`initial`, `last_name`, `suffix`, `phone`,`fax`,`email`, `region_id`)
	SELECT 2, 'Absentee Voter Clerk', lo.lovc_title, lo.lovc_first_name,
	lo.lovc_initial, lo.lovc_last_name, lo.lovc_suffix, lo.lovc_phone, lo.lovc_fax, lo.lovc_email, lo.id
	 FROM  `local_officials` lo WHERE lo.lovc_first_name <> '' OR lo.lovc_last_name <> ''
	 OR lo.lovc_phone <> '' OR lo.lovc_fax <> '' OR lo.lovc_email <> '';

INSERT INTO `officers` ( `order_number`, `office_name`, `title`, `first_name`,
	`initial`, `last_name`, `suffix`, `phone`,`fax`,`email`, `region_id`)
	SELECT 3, 'Additional Contacts', lo.addc_title, lo.addc_first_name,
	lo.addc_initial, lo.addc_last_name, lo.addc_suffix, lo.addc_phone, '', lo.addc_email, lo.id
	 FROM  `local_officials` lo WHERE lo.addc_first_name <> '' OR lo.addc_last_name <> ''
	 OR lo.addc_phone <> '' OR lo.addc_email <> '';


CREATE TABLE `local_officials_to_officers` (
	`local_official_id` INT(11) NULL,
	`officer_id` INT(11) UNSIGNED NULL
) ENGINE=InnoDB;

ALTER TABLE `local_officials_to_officers`
	ADD CONSTRAINT `local_official_fk` FOREIGN KEY (`local_official_id`) REFERENCES `local_officials` (`id`),
	ADD CONSTRAINT `officer_fk` FOREIGN KEY (`officer_id`) REFERENCES `officers` (`id`);

INSERT INTO `local_officials_to_officers` SELECT region_id, `id` FROM `officers`;

ALTER TABLE `local_officials`
	ADD COLUMN `general_email` VARCHAR(255) NOT NULL DEFAULT '' AFTER `addc_email`;