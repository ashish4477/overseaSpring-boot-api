CREATE TABLE `extended_profile` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`created` DATETIME NULL,
	`user_id` INT(11) NOT NULL,
	`political_party` VARCHAR(255) NOT NULL DEFAULT '',
	`voting_method` VARCHAR(255) NOT NULL DEFAULT '',
	`voter_type` VARCHAR(255) NOT NULL DEFAULT '',
	`voter_participation` VARCHAR(255) NOT NULL DEFAULT '',
	`voter_participation_other` VARCHAR(255) NOT NULL DEFAULT '',
	`outreach_participation` VARCHAR(255) NOT NULL DEFAULT '',
	`outreach_participation_other` VARCHAR(255) NOT NULL DEFAULT '',
	`social_media` VARCHAR(255) NOT NULL DEFAULT '',
	`social_media_other` VARCHAR(255) NOT NULL DEFAULT '',
	`volunteering` VARCHAR(255) NOT NULL DEFAULT '',
	`volunteering_other` VARCHAR(255) NOT NULL DEFAULT '',
	`satisfaction` VARCHAR(255) NOT NULL DEFAULT '',
	PRIMARY KEY (`id`),
	CONSTRAINT `FK_extended_profile_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)
ENGINE=InnoDB;
