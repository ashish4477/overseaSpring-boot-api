CREATE TABLE `tracked_forms` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`first_name` VARCHAR(255) NULL,
	`last_name` VARCHAR(255) NULL,
	`email_address` VARCHAR(255) NOT NULL,
	`flow_type` VARCHAR(255) NOT NULL,
	`face_id` INT(11) NOT NULL,
	`last_email_date` TIMESTAMP NOT NULL,
	`number_email` INT(11) DEFAULT 0,
	PRIMARY KEY (`id`),
	KEY `tracked_form_face_id` (`face_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

ALTER TABLE `tracked_forms`
	ADD CONSTRAINT `tracked_form_face_id` FOREIGN KEY (`face_id`) REFERENCES `faces_config` (`id`);