ALTER TABLE `mailinglist` RENAME TO mailing_list_address;

ALTER TABLE `mailing_list_address`
	CHANGE COLUMN `id` `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT FIRST;

CREATE TABLE mailing_list (
  id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` TEXT NOT NULL,
  api_key VARCHAR(255) NOT NULL,
  company_identifier VARCHAR(255) NOT NULL,
  field_type_id INT(11),
  PRIMARY KEY(id)
) ENGINE=InnoDB;

CREATE TABLE mailing_list_link (
  id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  mailing_list_id INT(11) UNSIGNED NOT NULL,
  mailing_address_id INT(11) UNSIGNED NOT NULL,
  `status` TINYINT(4) NULL DEFAULT '0',
  last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

ALTER TABLE mailing_list
  ADD CONSTRAINT `field_type_fk` FOREIGN KEY (`field_type_id`) REFERENCES `question_field_types` (`id`);

ALTER TABLE `mailing_list_link`
  ADD CONSTRAINT `mailing_list_fk` FOREIGN KEY (`mailing_list_id`) REFERENCES `mailing_list` (`id`),
  ADD CONSTRAINT `mailing_address_fk` FOREIGN KEY (`mailing_address_id`) REFERENCES `mailing_list_address` (`id`);