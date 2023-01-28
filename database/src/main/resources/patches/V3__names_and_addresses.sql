CREATE TABLE `wizard_result_addresses` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`address_to` VARCHAR(255) NULL DEFAULT '',
	`street1` VARCHAR(255) NULL DEFAULT '',
	`street2` VARCHAR(255) NULL DEFAULT '',
	`city` VARCHAR(255) NULL DEFAULT '',
	`state` VARCHAR(32) NULL DEFAULT '',
	`zip` VARCHAR(10) NULL DEFAULT '',
	`zip4` VARCHAR(5) NULL DEFAULT '',
	`country` VARCHAR(255) NULL DEFAULT '',
	`type` VARCHAR(32) NOT NULL DEFAULT 'UNKNOWN',
	`description` TEXT NULL,
	`result_id` INT(11) NOT NULL,
	`tmp_kind` VARCHAR(32) NULL,
	PRIMARY KEY (`id`),
	KEY (`result_id`)
) ENGINE=InnoDB;

ALTER TABLE `users`
  ADD COLUMN `previous_address` INT(11) NULL DEFAULT NULL AFTER `forwarding_address`,
  ADD COLUMN `name_id` INT(11) NOT NULL AFTER `password`,
  ADD COLUMN `previous_name_id` INT(11) NOT NULL AFTER `name_id`,
  ADD COLUMN `alternate_phone` VARCHAR(32) NOT NULL DEFAULT '' AFTER `phone`;

ALTER TABLE `pdf_answers`
  ADD COLUMN `name_id` INT(11) NOT NULL AFTER `username`,
  ADD COLUMN `previous_name_id` INT(11) NOT NULL AFTER `name_id`,
  ADD COLUMN `alternate_phone` VARCHAR(32) NULL DEFAULT '' AFTER `phone`,
  ADD COLUMN `voting_address_id` INT(11) NULL DEFAULT NULL AFTER `party`,
  ADD COLUMN `current_address_id` INT(11) NULL DEFAULT NULL AFTER `voting_address_id`,
  ADD COLUMN `forwarding_address_id` INT(11) NULL DEFAULT NULL AFTER `current_address_id`,
  ADD COLUMN `previous_address_id` INT(11) NULL DEFAULT NULL AFTER `forwarding_address_id`;

CREATE TABLE `user_names` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`title` VARCHAR(32) NULL DEFAULT '',
	`first_name` VARCHAR(128) NULL DEFAULT '',
	`last_name` VARCHAR(255) NULL DEFAULT '',
	`middle_name` VARCHAR(255) NULL DEFAULT '',
	`suffix` VARCHAR(32) NULL DEFAULT '',
	`temp_id` INT(11) NOT NULL,
	`temp_kind` VARCHAR(32) NULL,
	PRIMARY KEY (`id`),
	KEY (`temp_id`)
)  ENGINE=InnoDB;

CREATE TABLE `wizard_result_names` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`title` VARCHAR(32) NULL DEFAULT '',
	`first_name` VARCHAR(128) NULL DEFAULT '',
	`last_name` VARCHAR(255) NULL DEFAULT '',
	`middle_name` VARCHAR(255) NULL DEFAULT '',
	`suffix` VARCHAR(32) NULL DEFAULT '',
	`temp_id` INT(11) NOT NULL,
	`temp_kind` VARCHAR(32) NULL,
	PRIMARY KEY (`id`),
	KEY (`temp_id`)
)  ENGINE=InnoDB;

INSERT INTO `user_names` (`first_name`,`middle_name`,`last_name`,`suffix`,`temp_id`,`temp_kind`)
SELECT `first_name`,`middle_name`,`last_name`,`suffix`, `id`, 'name' from `users`;

UPDATE `users` u SET `name_id`=(SELECT un.`id` from `user_names` un WHERE un.temp_id = u.id and un.temp_kind='name');

INSERT INTO `user_names` (`last_name`,`temp_id`,`temp_kind`)
SELECT `previous_name`, `id`, 'previous' from `users`;

UPDATE `users` u SET `previous_name_id`=(SELECT un.`id` from `user_names` un WHERE un.temp_id = u.id and un.temp_kind='previous');

INSERT INTO `wizard_result_names` (`first_name`,`middle_name`,`last_name`,`suffix`,`temp_id`,`temp_kind`)
SELECT `first_name`,`middle_name`,`last_name`,`suffix`, `id`, 'name' from `pdf_answers`;

UPDATE `pdf_answers` u SET `name_id`=(SELECT un.`id` from `wizard_result_names` un WHERE un.temp_id = u.id and un.temp_kind='name');

INSERT INTO `wizard_result_names` (`last_name`,`temp_id`,`temp_kind`)
SELECT `previous_name`, `id`, 'previous' from `pdf_answers`;

UPDATE `pdf_answers` u SET `previous_name_id`=(SELECT un.`id` from `wizard_result_names` un WHERE un.temp_id = u.id and un.temp_kind='previous');


INSERT INTO `wizard_result_addresses` ( street1, street2, city, state, zip, zip4, country, description, `type`, result_id, tmp_kind )
SELECT p.voting_address_street1, p.voting_address_street2, p.voting_address_city,
p.voting_address_state, p.voting_address_zip, p.voting_address_zip4, p.voting_address_country,
p.voting_address_description, p.voting_address_type, p.id, 'voting' FROM `pdf_answers` p;

INSERT INTO `wizard_result_addresses` ( street1, street2, city, state, zip, zip4, country, description, `type`, result_id, tmp_kind )
SELECT p.current_address_street1, p.current_address_street2, p.current_address_city,
p.current_address_state, p.current_address_zip, p.current_address_zip4, p.current_address_country,
p.current_address_description, p.current_address_type, p.id, 'current' FROM `pdf_answers` p;

INSERT INTO `wizard_result_addresses` ( street1, street2, city, state, zip, zip4, country, description, `type`, result_id, tmp_kind )
SELECT p.forwarding_address_street1, p.forwarding_address_street2, p.forwarding_address_city,
p.forwarding_address_state, p.forwarding_address_zip, p.forwarding_address_zip4, p.forwarding_address_country,
p.forwarding_address_description, p.forwarding_address_type, p.id, 'forwarding' FROM `pdf_answers` p;

UPDATE `pdf_answers` p SET p.voting_address_id =
(SELECT w.id FROM `wizard_result_addresses` w WHERE w.result_id = p.id and w.tmp_kind = 'voting');

UPDATE `pdf_answers` p SET p.current_address_id =
(SELECT w.id FROM `wizard_result_addresses` w WHERE w.result_id = p.id and w.tmp_kind = 'current');

UPDATE `pdf_answers` p SET p.forwarding_address_id =
(SELECT w.id FROM `wizard_result_addresses` w WHERE w.result_id = p.id and w.tmp_kind = 'forwarding');

ALTER TABLE `wizard_result_names`  DROP COLUMN `temp_id`,  DROP COLUMN `temp_kind`;
ALTER TABLE `wizard_result_addresses`  DROP COLUMN `result_id`,  DROP COLUMN `tmp_kind`;
ALTER TABLE `user_names`  DROP COLUMN `temp_id`,  DROP COLUMN `temp_kind`;

ALTER TABLE `users`
  DROP COLUMN `first_name`,
  DROP COLUMN `last_name`,
  DROP COLUMN `middle_name`,
  DROP COLUMN `suffix`,
  DROP COLUMN `previous_name`;

ALTER TABLE `pdf_answers`
  DROP COLUMN `first_name`,
  DROP COLUMN `last_name`,
  DROP COLUMN `middle_name`,
  DROP COLUMN `previous_name`,
  DROP COLUMN `suffix`,
  DROP COLUMN `voting_address_street1`,
  DROP COLUMN `voting_address_street2`,
  DROP COLUMN `voting_address_city`,
  DROP COLUMN `voting_address_state`,
  DROP COLUMN `voting_address_zip`,
  DROP COLUMN `voting_address_zip4`,
  DROP COLUMN `voting_address_country`,
  DROP COLUMN `voting_address_description`,
  DROP COLUMN `voting_address_type`,
  DROP COLUMN `current_address_street1`,
  DROP COLUMN `current_address_street2`,
  DROP COLUMN `current_address_city`,
  DROP COLUMN `current_address_state`,
  DROP COLUMN `current_address_zip`,
  DROP COLUMN `current_address_zip4`,
  DROP COLUMN `current_address_country`,
  DROP COLUMN `current_address_description`,
  DROP COLUMN `current_address_type`,
  DROP COLUMN `forwarding_address_street1`,
  DROP COLUMN `forwarding_address_street2`,
  DROP COLUMN `forwarding_address_city`,
  DROP COLUMN `forwarding_address_state`,
  DROP COLUMN `forwarding_address_zip`,
  DROP COLUMN `forwarding_address_zip4`,
  DROP COLUMN `forwarding_address_country`,
  DROP COLUMN `forwarding_address_type`,
  DROP COLUMN `forwarding_address_description`;
