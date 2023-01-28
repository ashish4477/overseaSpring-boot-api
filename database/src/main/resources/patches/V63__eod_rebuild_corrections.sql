DROP TABLE IF EXISTS `corrections_to_officers`;
DROP TABLE IF EXISTS `corrections_officers`;
CREATE TABLE `corrections_officers` (
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

INSERT INTO `corrections_officers` ( `order_number`, `office_name`, `title`, `first_name`,
	`initial`, `last_name`, `suffix`, `phone`,`fax`,`email`, `region_id`)
	SELECT 1, 'Local Election Official', co.leo_title, co.leo_first_name,
	co.leo_initial, co.leo_last_name, co.leo_suffix, co.leo_phone, co.leo_fax, co.leo_email, co.id
	 FROM  `leo_corrections` co WHERE co.leo_first_name <> '' OR co.leo_last_name <> ''
	 OR co.leo_phone <> '' OR co.leo_fax <> '' OR co.leo_email <> '';

INSERT INTO `corrections_officers` ( `order_number`, `office_name`, `title`, `first_name`,
	`initial`, `last_name`, `suffix`, `phone`,`fax`,`email`, `region_id`)
	SELECT 2, 'Absentee Voter Clerk', co.lovc_title, co.lovc_first_name,
	co.lovc_initial, co.lovc_last_name, co.lovc_suffix, co.lovc_phone, co.lovc_fax, co.lovc_email, co.id
	 FROM  `leo_corrections` co WHERE co.lovc_first_name <> '' OR co.lovc_last_name <> ''
	 OR co.lovc_phone <> '' OR co.lovc_fax <> '' OR co.lovc_email <> '';

INSERT INTO `corrections_officers` ( `order_number`, `office_name`, `title`, `first_name`,
	`initial`, `last_name`, `suffix`, `phone`,`fax`,`email`, `region_id`)
	SELECT 3, 'Additional Contacts', co.addc_title, co.addc_first_name,
	co.addc_initial, co.addc_last_name, co.addc_suffix, co.addc_phone, '', co.addc_email, co.id
	 FROM  `leo_corrections` co WHERE co.addc_first_name <> '' OR co.addc_last_name <> ''
	 OR co.addc_phone <> '' OR co.addc_email <> '';


CREATE TABLE `corrections_to_officers` (
	`correction_id` INT(11) NULL,
	`officer_id` INT(11) UNSIGNED NULL
) ENGINE=InnoDB;

ALTER TABLE `corrections_to_officers`
	ADD CONSTRAINT `leo_corrections_fk` FOREIGN KEY (`correction_id`) REFERENCES `leo_corrections` (`id`),
	ADD CONSTRAINT `correction_officer_fk` FOREIGN KEY (`officer_id`) REFERENCES `corrections_officers` (`id`);

INSERT INTO `corrections_to_officers` SELECT region_id, `id` FROM `corrections_officers`;

ALTER TABLE `leo_corrections`
	ADD COLUMN `general_email` VARCHAR(255) NOT NULL DEFAULT '' AFTER `addc_email`;