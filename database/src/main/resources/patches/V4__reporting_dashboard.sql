CREATE TABLE `rd_reports` (
   `id` INT(11) NOT NULL AUTO_INCREMENT,
   `owner_id` INT(11) DEFAULT NULL,
   `title` VARCHAR(255) NOT NULL,
   `description` TEXT DEFAULT NULL,
   `flow_type` VARCHAR(32) DEFAULT NULL,
   `date_from` DATE DEFAULT NULL,
   `date_to` DATE DEFAULT NULL,
   `standard` int(4) NOT NULL DEFAULT 0,
   PRIMARY KEY (`id`),
   KEY `report_owner_id` (`owner_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `rd_report_faces` (
   `report_id` INT(11) NOT NULL,
   `face` VARCHAR(32) NOT NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
   
CREATE TABLE `rd_report_columns` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`report_id` INT(11) NOT NULL,
	`name` VARCHAR(255) NOT NULL DEFAULT '',
	`column_number` INT(11) NOT NULL DEFAULT -1,
	PRIMARY KEY (`id`),
	KEY `column_report_id` (`report_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `rd_report_fields` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`column_id` INT(11) NOT NULL,
	`user_field_name` VARCHAR(32) DEFAULT NULL,
	`question_id` INT(11) DEFAULT NULL,
	PRIMARY KEY (`id`),
	KEY `report_field_column_id` (`column_id`),
	KEY `report_field_question_id` (`question_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE `rd_report_answers` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`report_field_id` INT(11) NOT NULL,
	`answer` VARCHAR(255) DEFAULT NULL,
	`predefined_id` INT(11) DEFAULT NULL,
	PRIMARY KEY (`id`),
	KEY `answer_report_field_id` (`report_field_id`),
	KEY `answer_predefined_id` (`predefined_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

ALTER TABLE `rd_reports`
  ADD CONSTRAINT `report_owner_fk` FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`);
  
ALTER TABLE `rd_report_columns`
  ADD CONSTRAINT `column_report_fk` FOREIGN KEY (`report_id`) REFERENCES `rd_reports` (`id`);

ALTER TABLE `rd_report_fields`
  ADD CONSTRAINT `report_field_column_fk` FOREIGN KEY (`column_id`) REFERENCES `rd_report_columns` (`id`);
ALTER TABLE `rd_report_fields`
  ADD CONSTRAINT `report_field_question_fk` FOREIGN KEY (`question_id`) REFERENCES `question_fields` (`id`);
  
ALTER TABLE `rd_report_answers`
  ADD CONSTRAINT `answer_report_field_fk` FOREIGN KEY (`report_field_id`) REFERENCES `rd_report_fields` (`id`);
ALTER TABLE `rd_report_answers`
  ADD CONSTRAINT `answer_predefined_fk` FOREIGN KEY (`predefined_id`) REFERENCES `field_dictionaries` (`id`);