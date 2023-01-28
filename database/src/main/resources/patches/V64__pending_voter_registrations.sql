CREATE TABLE `pending_voter_addresses` (
   `id` INT(11) NOT NULL AUTO_INCREMENT,
   `encrypted` BLOB NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `pending_voter_answers` (
   `id` INT(11) NOT NULL AUTO_INCREMENT,
   `pending_voter_registration_id` INT(11) DEFAULT NULL,
   `encrypted` BLOB NOT NULL,
   `sort_order` INT(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `pending_voter_names` (
   `id` INT(11) NOT NULL AUTO_INCREMENT,
   `encrypted` BLOB DEFAULT NULL,
   `first_name` VARCHAR(128) DEFAULT NULL,
   `last_name` VARCHAR(128) DEFAULT NULL,
   `middle_name` VARCHAR(128) DEFAULT NULL,
   `suffix` VARCHAR(32) DEFAULT NULL,
   `title` VARCHAR(32) DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `pending_voter_registrations` (
   `id` INT(11) NOT NULL AUTO_INCREMENT,
   `created_date` TIMESTAMP,
   `current_address_id` INT(11) NOT NULL UNIQUE,
   `encrypted` BLOB NOT NULL,
   `face_prefix` VARCHAR(128) DEFAULT NULL,
   `forwarding_address_id` INT(11) DEFAULT NULL,
   `gender` VARCHAR(16) DEFAULT NULL,
   `name_id` INT(11) NOT NULL UNIQUE,
   `previous_address_id` INT(11) DEFAULT NULL,
   `previous_name_id` INT(11) DEFAULT NULL,
   `voter_history` VARCHAR(128) DEFAULT NULL,
   `voter_type` VARCHAR(128) DEFAULT NULL,
   `voting_address_id` INT(11) NOT NULL UNIQUE,
   `voting_region` VARCHAR(128) NOT NULL,
   `voting_state` VARCHAR(16) NOT NULL,
   PRIMARY KEY (`id`),
   INDEX (`current_address_id`, `forwarding_address_id`, `name_id`, `previous_address_id`, `previous_name_id`, `voting_address_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `pending_voter_registration_statuses` (
   `id` INT(11) NOT NULL AUTO_INCREMENT,
   `completion_date` TIMESTAMP,
   `downloaded_by_id` INT(11) DEFAULT NULL,
   `name_id` INT(11) NOT NULL UNIQUE,
   PRIMARY KEY (`id`),
   INDEX (`downloaded_by_id`, `name_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `encryption_key_statuses` (
	`date_string` VARCHAR(16) NOT NULL,
	`state_abbr` VARCHAR(8) NOT NULL,
	`voting_region_name` VARCHAR(128) NOT NULL,
	`status` BOOLEAN NOT NULL,
	PRIMARY KEY(`date_string`, `state_abbr`, `voting_region_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

INSERT INTO `roles` (`id`, `name`) VALUES(20, 'pending_voter_registrations');

ALTER TABLE `pending_voter_registrations`
   ADD CONSTRAINT `pvr_current_address_fk` FOREIGN KEY (`current_address_id`) REFERENCES `pending_voter_addresses` (`id`),
   ADD CONSTRAINT `pvr_forwarding_address_fk` FOREIGN KEY (`forwarding_address_id`) REFERENCES `pending_voter_addresses` (`id`),
   ADD CONSTRAINT `pvr_name_fk` FOREIGN KEY (`name_id`) REFERENCES `pending_voter_names` (`id`),
   ADD CONSTRAINT `pvr_previous_address_fk` FOREIGN KEY (`previous_address_id`) REFERENCES `pending_voter_addresses` (`id`),
   ADD CONSTRAINT `pvr_previous_name_fk` FOREIGN KEY (`previous_name_id`) REFERENCES `pending_voter_names` (`id`),
   ADD CONSTRAINT `pvr_voting_address_fk` FOREIGN KEY (`voting_address_id`) REFERENCES `pending_voter_addresses` (`id`);

ALTER TABLE `pending_voter_registration_statuses`
   ADD CONSTRAINT `pvrs_downloaded_by_fk` FOREIGN KEY (`downloaded_by_id`) REFERENCES `users` (`id`),
   ADD CONSTRAINT `pvrs_name_fk` FOREIGN KEY (`name_id`) REFERENCES `pending_voter_names` (`id`);