CREATE TABLE `mail_templates` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255),
  `subject` VARCHAR(255) ,
  `from_address` VARCHAR(255) ,
  `reply_to_address` VARCHAR(255) ,
  `body_template` TEXT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `mailing_tasks` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `mailing_list_id` INT(11) UNSIGNED NOT NULL,
  `template_id` INT(11) NOT NULL,
  `start_on` DATETIME,
  `status` INT(5),
  `subject` VARCHAR(255),
  PRIMARY KEY (`id`),
  CONSTRAINT `task_mailing_list_fk` FOREIGN KEY (`mailing_list_id`) REFERENCES `mailing_list` (`id`),
  CONSTRAINT `task_template_fk` FOREIGN KEY (`template_id`) REFERENCES `mail_templates` (`id`)
) ENGINE=InnoDB;

UPDATE `mailing_list_link` SET `status` = 1 WHERE `status` = 0 OR `status` = 3;

ALTER TABLE `mailing_list`
ADD COLUMN `from_address` VARCHAR(255) NULL AFTER `field_type_id`,
ADD COLUMN `reply_to_address` VARCHAR(255) NOT NULL AFTER `from_address`,
ADD COLUMN `signature` TEXT NULL AFTER `reply_to_address`;

ALTER TABLE `mailing_list_address`
ADD COLUMN `eod_region` VARCHAR(127) NULL AFTER `region`;
