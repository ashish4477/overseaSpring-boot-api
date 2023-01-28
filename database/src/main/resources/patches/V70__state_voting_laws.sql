CREATE TABLE `state_voting_laws` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`state_id` INT(11) NOT NULL DEFAULT '0',
	`early_in_person_voting` INT(4) NOT NULL DEFAULT '0',
	`no_excuse_absentee` INT(4) NOT NULL DEFAULT '0',
	`absentee_with_excuse` INT(4) NOT NULL DEFAULT '0',
	`same_day_registration` INT(6) NOT NULL DEFAULT '0',
	`all_mail_voting` INT(6) NOT NULL DEFAULT '0',
	`voter_id` INT(6) NOT NULL DEFAULT '0',
	`voter_id_additional` TEXT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB;

ALTER TABLE `state_voting_laws`
	ADD CONSTRAINT `FK_state_voting_laws_states` FOREIGN KEY (`state_id`) REFERENCES `states` (`id`);

insert into state_voting_laws (state_id) select id from states;
